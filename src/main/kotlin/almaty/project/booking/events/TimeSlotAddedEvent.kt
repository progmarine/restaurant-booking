package almaty.project.booking.events

import java.time.LocalDateTime

data class TimeSlotAddedEvent(
        val timeSlotId: Long,
        val restaurantId: String,
        val time: LocalDateTime? = null,
        val isAvailable: Boolean? = null
) : Event