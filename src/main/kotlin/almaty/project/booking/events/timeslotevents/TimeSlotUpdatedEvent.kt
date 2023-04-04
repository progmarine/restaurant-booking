package almaty.project.booking.events.timeslotevents

import almaty.project.booking.domain.TimeSlotDto
import almaty.project.booking.events.Event

data class TimeSlotUpdatedEvent(
        val restaurantId: String,
        val timeSlotDto: TimeSlotDto
) : Event