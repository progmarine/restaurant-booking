package almaty.project.booking.commands

import almaty.project.booking.domain.TimeSlot
import reactor.core.publisher.Mono
import java.time.LocalDateTime

data class AddTimeSlotCommand(
        val restaurantId: String,
        val partySize: Int?=null,
        val time: LocalDateTime ?=null
) {
    fun toEntity() =
            Mono.just (TimeSlot(restaurantId = restaurantId,time = time, partySize = partySize, isAvailable = time!= null && time > LocalDateTime.now()))
}