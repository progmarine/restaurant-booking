package almaty.project.booking.commands

data class DeleteReviewCommand(
        val reviewId: Long,
        val restaurantId: String,
        val userId: Long
)