package almaty.project.booking.events

data class ReviewDeletedEvent(
        val reviewId: Long,
        val restaurantId: String
): Event