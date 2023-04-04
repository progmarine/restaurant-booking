package almaty.project.booking.events.reviewevents

import almaty.project.booking.events.Event

data class ReviewEditedEvent(
        val reviewId: Long,
        val restaurantId: String,
        val userId: Long,
        val newRating: Int,
        val newComment: String?=null
) : Event