package almaty.project.booking.domain

import java.time.LocalDateTime

data class TimeSlotDto(
        val timeslotId: Long,
        val time: LocalDateTime?=null,
        val isAvailable: Boolean
)