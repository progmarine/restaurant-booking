package almaty.project.booking.commands

import java.time.LocalDateTime

data class UpdateTimeSlotCommand(
        val timeslotId: Long,
        val restaurantId: String,
        val time: LocalDateTime?=null,
        val isAvailable: Boolean?=null,
        val partySize: Int?=null
)