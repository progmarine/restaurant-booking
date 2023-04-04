package almaty.project.booking.commands

import almaty.project.booking.domain.Booking
import almaty.project.booking.domain.BookingStatus
import reactor.core.publisher.Mono

data class CreateBookingCommand(
        val userId: Long,
        val restaurantId: String,
        val timeSlotId: Long,
        val partySize: Int,
        val specialRequests: String?=null,
) {
    fun toBookingEntity(status: BookingStatus): Mono<Booking> {
        return Mono.just(
                Booking(
                        userId = userId,
                        restaurantId = restaurantId,
                        timeSlotId = timeSlotId,
                        status = status.toString(),
                        partySize = partySize,
                        specialRequest = specialRequests
                )
        )
    }
}