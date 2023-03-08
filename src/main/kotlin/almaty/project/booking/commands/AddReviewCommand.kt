package almaty.project.booking.commands

import almaty.project.booking.domain.Review
import reactor.core.publisher.Mono

data class AddReviewCommand(
        val userId: Long,
        val restaurantId: String,
        val rating: Int,
        val comment: String) {
    fun toReviewEntity(): Mono<Review> {
        return Mono.just(
                Review(
                        userId = userId,
                        restaurantId = restaurantId,
                        rating = rating,
                        comment = comment
                )
        )
    }
}
