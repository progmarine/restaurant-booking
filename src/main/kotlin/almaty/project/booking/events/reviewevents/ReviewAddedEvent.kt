package almaty.project.booking.events.reviewevents

import almaty.project.booking.events.Event

data class ReviewAddedEvent(
        val reviewId: Long,
        val restaurantId: String,
        val userId: Long,
        val reviewText: String?=null,
        val rating: Int
): Event