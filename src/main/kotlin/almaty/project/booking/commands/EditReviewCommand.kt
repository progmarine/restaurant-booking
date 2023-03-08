package almaty.project.booking.commands

data class EditReviewCommand(
        val reviewId: Long,
        val restaurantId: String,
        val userId: Long,
        val newRating: Int,
        val newComment: String?=null
)