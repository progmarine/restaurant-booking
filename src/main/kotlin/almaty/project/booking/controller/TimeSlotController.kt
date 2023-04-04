package almaty.project.booking.controller

import almaty.project.booking.commands.AddTimeSlotCommand
import almaty.project.booking.commands.UpdateTimeSlotCommand
import almaty.project.booking.events.timeslotevents.TimeSlotAddedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotUpdatedEvent
import almaty.project.booking.service.TimeSlotService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/timeslots")
class TimeSlotController(private val timeSlotService: TimeSlotService) {

    @PostMapping
    fun addTimeSlot(@RequestBody command: AddTimeSlotCommand): Mono<TimeSlotAddedEvent> {
        return timeSlotService.addTimeSlot(command)
    }

    @PutMapping
    fun updateTimeSlot(@RequestBody command: UpdateTimeSlotCommand): Mono<TimeSlotUpdatedEvent> {
        return timeSlotService.updateTimeSlot(command)
    }

}