package almaty.project.booking.controller

import almaty.project.booking.commands.AddTimeSlotCommand
import almaty.project.booking.domain.TimeSlot
import almaty.project.booking.service.TimeSlotService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/timeslots")
class TimeSlotController(private val timeSlotService: TimeSlotService) {

    @PostMapping
    fun addRestaurant(@RequestBody command: AddTimeSlotCommand): Mono<TimeSlot> {
        return timeSlotService.addTimeSlot(command)
    }

}