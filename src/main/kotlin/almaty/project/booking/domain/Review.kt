package almaty.project.booking.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("reviews")
data class Review(
        @Id val id: Long? = null,
        val userId: Long,
        val restaurantId: String,
        var rating: Int,
        var comment: String? = null
) {
    fun toDto() = ReviewDto(reviewId = id!!, userId, rating, comment)
}