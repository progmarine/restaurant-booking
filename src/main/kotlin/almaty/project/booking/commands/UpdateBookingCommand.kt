package almaty.project.booking.commands

import almaty.project.booking.domain.BookingStatus

data class UpdateBookingCommand(
        val bookingId: Long,
        val timeSlotId: Long?=null,
        val status: BookingStatus?=null,
        val partySize: Int?=null,
        val specialRequests: String?=null
)