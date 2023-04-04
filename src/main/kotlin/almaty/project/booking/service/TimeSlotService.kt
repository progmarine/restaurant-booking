package almaty.project.booking.service

import almaty.project.booking.commands.AddTimeSlotCommand
import almaty.project.booking.commands.UpdateTimeSlotCommand
import almaty.project.booking.domain.TimeSlot
import almaty.project.booking.events.bookingevents.BookingCreatedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotAddedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotUpdatedEvent
import reactor.core.publisher.Mono

interface TimeSlotService {
    fun addTimeSlot(command: AddTimeSlotCommand): Mono<TimeSlotAddedEvent>

    fun updateTimeSlot(command: UpdateTimeSlotCommand): Mono<TimeSlotUpdatedEvent>

    fun updateTimeSlot(event: BookingCreatedEvent): Mono<TimeSlot>
}