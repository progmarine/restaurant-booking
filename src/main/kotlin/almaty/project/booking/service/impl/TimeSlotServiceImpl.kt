package almaty.project.booking.service.impl

import almaty.project.booking.commands.AddTimeSlotCommand
import almaty.project.booking.commands.UpdateTimeSlotCommand
import almaty.project.booking.domain.TimeSlot
import almaty.project.booking.domain.TimeSlotDto
import almaty.project.booking.events.bookingevents.BookingCreatedEvent
import almaty.project.booking.events.producer.EventProducer
import almaty.project.booking.events.timeslotevents.TimeSlotAddedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotUpdatedEvent
import almaty.project.booking.repository.TimeSlotRepository
import almaty.project.booking.service.TimeSlotService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TimeSlotServiceImpl(private val timeSlotRepository: TimeSlotRepository, private val eventProducer: EventProducer) : TimeSlotService {

    override fun addTimeSlot(command: AddTimeSlotCommand): Mono<TimeSlotAddedEvent> {
        return command.toEntity()
                .flatMap { timeSlotRepository.save(it) }
                .flatMap { timeSlot ->
                    val event = TimeSlotAddedEvent(timeSlot.id!!, timeSlot.restaurantId!!, timeSlot.time, timeSlot.partySize, timeSlot.isAvailable)
                    eventProducer.sendEvent(event)
                }
    }

    override fun updateTimeSlot(command: UpdateTimeSlotCommand): Mono<TimeSlotUpdatedEvent> {
        return timeSlotRepository.findById(command.timeslotId)
                .flatMap { timeSlot ->
                    if (command.restaurantId == timeSlot.restaurantId) {
                        command.time?.let { timeSlot.time = it }
                        command.isAvailable?.let { timeSlot.isAvailable = it }
                        command.partySize?.let { timeSlot.partySize = it }
                        timeSlotRepository.save(timeSlot)
                    } else {
                        logger.error("cannot find timeSlot for restaurant with id : ${command.restaurantId}")
                        throw Exception("cannot find timeSlot for restaurant with id : ${command.restaurantId}")
                    }
                }
                .flatMap { timeSlot ->
                    val event = TimeSlotUpdatedEvent(
                            restaurantId = timeSlot.restaurantId!!,
                            timeSlotDto = TimeSlotDto(
                                    timeslotId = timeSlot.id!!,
                                    time = timeSlot.time,
                                    partySize = timeSlot.partySize,
                                    isAvailable = timeSlot.isAvailable
                            )
                    )
                    eventProducer.sendEvent(event)
                }
    }

    override fun updateTimeSlot(event: BookingCreatedEvent): Mono<TimeSlot> {
        return timeSlotRepository.findById(event.timeSlotId)
                .flatMap { timeSlot ->
                    if (event.restaurantId == timeSlot.restaurantId && event.partySize == timeSlot.partySize) {
                        timeSlot.isAvailable = false
                        timeSlotRepository.save(timeSlot)
                    } else {
                        logger.error("cannot find timeSlot for restaurant with id : ${event.restaurantId}")
                        throw Exception("cannot find timeSlot for restaurant with id : ${event.restaurantId}")
                    }
                }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

}