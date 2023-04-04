package almaty.project.booking.events.bookingevents

import almaty.project.booking.domain.BookingStatus
import almaty.project.booking.events.Event

data class BookingConfirmedEvent(
        val bookingId: Long,
        val status: BookingStatus?=null,
        val partySize: Int?=null,
        val specialRequests: String?=null
): Event