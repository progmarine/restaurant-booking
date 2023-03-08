package almaty.project.booking.service

import almaty.project.booking.commands.AddTimeSlotCommand
import almaty.project.booking.domain.TimeSlot
import almaty.project.booking.events.EventProducer
import almaty.project.booking.repository.TimeSlotRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TimeSlotService(private val timeSlotRepository: TimeSlotRepository, private val eventProducer: EventProducer) {

    fun addTimeSlot(command: AddTimeSlotCommand): Mono<TimeSlot> {
        return command.toEntity()
                .flatMap { timeSlotRepository.save(it) }
                .doOnSuccess { eventProducer.sendTimeSlotAddedEvent(command, it) }
    }

}