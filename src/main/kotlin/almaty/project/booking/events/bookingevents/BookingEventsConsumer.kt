package almaty.project.booking.events.bookingevents

import almaty.project.booking.commands.UpdateBookingCommand
import almaty.project.booking.config.EventConsumerConfig
import almaty.project.booking.config.EventProducerConfig
import almaty.project.booking.domain.BookingStatus
import almaty.project.booking.domain.TimeSlotDto
import almaty.project.booking.events.Event
import almaty.project.booking.events.producer.EventProducer
import almaty.project.booking.events.timeslotevents.TimeSlotUpdatedEvent
import almaty.project.booking.service.BookingService
import almaty.project.booking.service.RestaurantService
import almaty.project.booking.service.TimeSlotService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class BookingEventsConsumer(
        private val timeSlotService: TimeSlotService,
        private val bookingService: BookingService,
        private val eventProducer: EventProducer,
        private val restaurantService: RestaurantService
) {

    @KafkaListener(
            topics = [EventProducerConfig.BOOKING_CREATED_TOPIC],
            containerFactory = "eventListenerContainerFactory",
            groupId = EventConsumerConfig.GROUP_ID,
            autoStartup = "\${spring.kafka.events.enable-listener}",
    )
    fun listenBookingCreated(record: ConsumerRecord<String, Event>) {
        try {
            val event = record.value() as BookingCreatedEvent
            timeSlotService.updateTimeSlot(event)
                    .flatMap { timeSlot ->
                        val updatedTimeSlotEvent = TimeSlotUpdatedEvent(
                                restaurantId = timeSlot.restaurantId!!,
                                timeSlotDto = TimeSlotDto(
                                        timeslotId = timeSlot.id!!,
                                        time = timeSlot.time,
                                        partySize = timeSlot.partySize,
                                        isAvailable = timeSlot.isAvailable
                                )
                        )
                        restaurantService.updateTimeSlot(updatedTimeSlotEvent)
                    }
                    .flatMap {
                        bookingService.updateBooking(
                                UpdateBookingCommand(
                                        bookingId = event.bookingId,
                                        status = BookingStatus.CONFIRMED
                                )
                        )
                    }
                    .doOnSuccess {
                        val bookingConfirmedEvent = BookingConfirmedEvent(
                                bookingId = it.id!!,
                                status = BookingStatus.CONFIRMED,
                                partySize = it.partySize,
                                specialRequests = it.specialRequest
                        )
                        eventProducer.sendEvent(bookingConfirmedEvent)
                    }
                    .subscribe()

            logger.info("Received event [${record.topic()}]: ${record.value()}")
        } catch (e: Exception) {
            logger.error("Could not handle: ${e.message}", e)
        }
    }

    @KafkaListener(
            topics = [EventProducerConfig.BOOKING_CONFIRMED_TOPIC],
            containerFactory = "eventListenerContainerFactory",
            groupId = EventConsumerConfig.GROUP_ID,
            autoStartup = "\${spring.kafka.events.enable-listener}",
    )
    fun listenBookingConfirmed(record: ConsumerRecord<String, Event>) {
        try {
            logger.info("Received event [${record.topic()}]: ${record.value()}")
        } catch (e: Exception) {
            logger.error("Could not handle: ${e.message}", e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}