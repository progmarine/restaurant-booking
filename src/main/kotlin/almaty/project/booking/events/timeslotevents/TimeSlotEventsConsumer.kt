package almaty.project.booking.events.timeslotevents

import almaty.project.booking.config.EventConsumerConfig
import almaty.project.booking.config.EventProducerConfig
import almaty.project.booking.events.Event
import almaty.project.booking.service.RestaurantService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class TimeSlotEventsConsumer(private val restaurantService: RestaurantService) {

    @KafkaListener(
            topics = [EventProducerConfig.TIMESLOT_ADDED_TOPIC],
            containerFactory = "eventListenerContainerFactory",
            groupId = EventConsumerConfig.GROUP_ID,
            autoStartup = "\${spring.kafka.events.enable-listener}",
    )
    fun listenTimeSlotAdded(record: ConsumerRecord<String, Event>) {
        try {
            val event = record.value() as TimeSlotAddedEvent
            restaurantService.addTimeSlot(event).subscribe()
            logger.info("Received event [${record.topic()}]: ${record.value()}")
        } catch (e: Exception) {
            logger.error("Could not handle: ${e.message}", e)
        }
    }

    @KafkaListener(
            topics = [EventProducerConfig.TIMESLOT_UPDATED_TOPIC],
            containerFactory = "eventListenerContainerFactory",
            groupId = EventConsumerConfig.GROUP_ID,
            autoStartup = "\${spring.kafka.events.enable-listener}",
    )
    fun listenTimeSlotUpdated(record: ConsumerRecord<String, Event>) {
        try {
            val event = record.value() as TimeSlotUpdatedEvent
            restaurantService.updateTimeSlot(event).subscribe()
            logger.info("Received event [${record.topic()}]: ${record.value()}")
        } catch (e: Exception) {
            logger.error("Could not handle: ${e.message}", e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}