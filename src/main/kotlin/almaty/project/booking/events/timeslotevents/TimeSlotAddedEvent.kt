package almaty.project.booking.events.timeslotevents

import almaty.project.booking.events.Event
import java.time.LocalDateTime

data class TimeSlotAddedEvent(
        val timeSlotId: Long,
        val restaurantId: String,
        val time: LocalDateTime? = null,
        val partySize: Int?=null,
        val isAvailable: Boolean? = null
) : Event