package almaty.project.booking.domain

data class ReviewDto(
        val reviewId: Long,
        val userId: Long,
        val rating: Int,
        val comment: String?=null
)