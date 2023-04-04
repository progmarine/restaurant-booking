package almaty.project.booking.service.impl

import almaty.project.booking.commands.AddReviewCommand
import almaty.project.booking.commands.DeleteReviewCommand
import almaty.project.booking.commands.EditReviewCommand
import almaty.project.booking.domain.Review
import almaty.project.booking.events.producer.EventProducer
import almaty.project.booking.events.reviewevents.ReviewAddedEvent
import almaty.project.booking.events.reviewevents.ReviewDeletedEvent
import almaty.project.booking.events.reviewevents.ReviewEditedEvent
import almaty.project.booking.repository.ReviewRepository
import almaty.project.booking.service.ReviewService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ReviewServiceImpl(private val reviewRepository: ReviewRepository, private val eventProducer: EventProducer) : ReviewService {

    override fun addReview(command: AddReviewCommand): Mono<ReviewAddedEvent> {
        return command.toReviewEntity()
                .flatMap { reviewRepository.save(it) }
                .flatMap { review ->
                    val event = ReviewAddedEvent(reviewId = review.id!!, restaurantId = review.restaurantId,
                            userId = review.userId,
                            reviewText = review.comment,
                            rating = review.rating)
                    eventProducer.sendEvent(event)
                }
    }

    override fun getReviewById(reviewId: Long): Mono<Review> {
        return reviewRepository.findById(reviewId)
    }

    override fun getReviewsByUserId(userId: Long): Flux<Review> {
        return reviewRepository.findByUserId(userId)
    }

    override fun getReviewsByRestaurantId(restaurantId: String): Flux<Review> {
        return reviewRepository.findByRestaurantId(restaurantId)
    }

    override fun editReview(command: EditReviewCommand): Mono<ReviewEditedEvent> {
        return reviewRepository.findById(command.reviewId)
                .flatMap { review ->
                    if (command.userId == review.userId) {
                        review.rating = command.newRating
                        review.comment = command.newComment
                        reviewRepository.save(review)
                    } else {
                        logger.error("cannot find review for user with id : ${command.userId}")
                        throw Exception("cannot find review for user with id : ${command.userId}")
                    }
                }
                .flatMap { review ->
                    val event = ReviewEditedEvent(restaurantId = review.restaurantId, reviewId = review.id!!,
                            userId = review.userId,
                            newComment = review.comment,
                            newRating = review.rating)
                    eventProducer.sendEvent(event)
                }
    }

    override fun deleteReview(command: DeleteReviewCommand) =
            reviewRepository.deleteById(command.reviewId).doOnSuccess {
                val event = ReviewDeletedEvent(reviewId = command.reviewId, restaurantId = command.restaurantId)
                eventProducer.sendEvent(event)
            }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

}