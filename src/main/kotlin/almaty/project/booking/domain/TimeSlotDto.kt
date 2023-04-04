package almaty.project.booking.domain

import java.time.LocalDateTime

data class TimeSlotDto(
        val timeslotId: Long,
        val time: LocalDateTime?,
        val partySize: Int?,
        val isAvailable: Boolean?
)