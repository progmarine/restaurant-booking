package almaty.project.booking.events.bookingevents

import almaty.project.booking.events.Event

data class BookingCreatedEvent(
        val bookingId: Long,
        val userId: Long,
        val restaurantId: String,
        val timeSlotId: Long,
        val partySize: Int,
        val specialRequest: String?=null
) : Event