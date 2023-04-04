package almaty.project.booking.service

import almaty.project.booking.commands.AddReviewCommand
import almaty.project.booking.commands.DeleteReviewCommand
import almaty.project.booking.commands.EditReviewCommand
import almaty.project.booking.domain.Review
import almaty.project.booking.events.reviewevents.ReviewAddedEvent
import almaty.project.booking.events.reviewevents.ReviewEditedEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReviewService  {
    fun addReview(command: AddReviewCommand): Mono<ReviewAddedEvent>
    fun getReviewById(reviewId: Long): Mono<Review>
    fun getReviewsByUserId(userId: Long): Flux<Review>
    fun getReviewsByRestaurantId(restaurantId: String): Flux<Review>
    fun editReview(command: EditReviewCommand): Mono<ReviewEditedEvent>
    fun deleteReview(command: DeleteReviewCommand) : Mono<Void>
}