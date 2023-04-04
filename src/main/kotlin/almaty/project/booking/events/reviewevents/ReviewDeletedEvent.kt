package almaty.project.booking.events.reviewevents

import almaty.project.booking.events.Event

data class ReviewDeletedEvent(
        val reviewId: Long,
        val restaurantId: String
): Event