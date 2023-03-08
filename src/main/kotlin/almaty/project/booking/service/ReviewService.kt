package almaty.project.booking.service

import almaty.project.booking.commands.AddReviewCommand
import almaty.project.booking.commands.DeleteReviewCommand
import almaty.project.booking.commands.EditReviewCommand
import almaty.project.booking.domain.Review
import almaty.project.booking.events.EventProducer
import almaty.project.booking.repository.ReviewRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ReviewService(private val reviewRepository: ReviewRepository, private val eventProducer: EventProducer) {

    fun addReview(command: AddReviewCommand): Mono<Review> {
        return command.toReviewEntity()
                .flatMap { reviewRepository.save(it) }
                .doOnSuccess { eventProducer.sendReviewAddedEvent(it) }
    }

    fun getReviewById(reviewId: Long): Mono<Review> {
        return reviewRepository.findById(reviewId)
    }

    fun getReviewsByUserId(userId: Long): Flux<Review> {
        return reviewRepository.findByUserId(userId)
    }

    fun getReviewsByRestaurantId(restaurantId: String): Flux<Review> {
        return reviewRepository.findByRestaurantId(restaurantId)
    }

    fun editReview(command: EditReviewCommand): Mono<Review> {
        return reviewRepository.findById(command.reviewId)
                .flatMap { review ->
                    if (command.userId == review.userId) {
                        review.rating = command.newRating
                        review.comment = command.newComment
                        reviewRepository.save(review)
                    }
                    else {
                        logger.error("cannot find review for user with id : ${command.userId}")
                        throw Exception("cannot find review for user with id : ${command.userId}")
                    }
                }
                .doOnSuccess { eventProducer.sendReviewEditedEvent(it) }
    }

    fun deleteReview(command: DeleteReviewCommand): Mono<Void> {
        return reviewRepository.deleteById(command.reviewId)
                .doOnSuccess { eventProducer.sendReviewDeletedEvent(command) }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

}