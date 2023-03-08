package almaty.project.booking.events

data class ReviewAddedEvent(
        val reviewId: Long,
        val restaurantId: String,
        val userId: Long,
        val reviewText: String?=null,
        val rating: Int
): Event