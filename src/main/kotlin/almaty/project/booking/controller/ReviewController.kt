package almaty.project.booking.controller

import almaty.project.booking.commands.AddReviewCommand
import almaty.project.booking.commands.DeleteReviewCommand
import almaty.project.booking.commands.EditReviewCommand
import almaty.project.booking.domain.Review
import almaty.project.booking.events.reviewevents.ReviewAddedEvent
import almaty.project.booking.events.reviewevents.ReviewEditedEvent
import almaty.project.booking.service.ReviewService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/reviews")
class ReviewController(private val reviewService: ReviewService) {

    @PostMapping
    fun addReview(@RequestBody command: AddReviewCommand): Mono<ReviewAddedEvent> {
        return reviewService.addReview(command)
    }

    @GetMapping("/{reviewId}")
    fun getReviewById(@PathVariable reviewId: Long): Mono<Review> {
        return reviewService.getReviewById(reviewId)
    }

    @GetMapping("/user/{userId}")
    fun getReviewsByUserId(@PathVariable userId: Long): Flux<Review> {
        return reviewService.getReviewsByUserId(userId)
    }

    @GetMapping("/restaurant/{restaurantId}")
    fun getReviewsByRestaurantId(@PathVariable restaurantId: String): Flux<Review> {
        return reviewService.getReviewsByRestaurantId(restaurantId)
    }

    @PutMapping
    fun editReview(@RequestBody command: EditReviewCommand): Mono<ReviewEditedEvent> {
        return reviewService.editReview(command)
    }

    @DeleteMapping
    fun deleteRestaurant(@RequestBody command: DeleteReviewCommand): Mono<Void> =
         reviewService.deleteReview(command)


}
