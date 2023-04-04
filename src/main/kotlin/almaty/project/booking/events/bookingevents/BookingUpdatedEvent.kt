package almaty.project.booking.events.bookingevents

import almaty.project.booking.domain.BookingStatus
import almaty.project.booking.events.Event

data class BookingUpdatedEvent(
        val bookingId: Long,
        val userId: Long,
        val restaurantId: String,
        val timeSlotId: Long?=null,
        val status: BookingStatus?=null,
        val partySize: Int?=null,
        val specialRequest: String?=null
) : Event
