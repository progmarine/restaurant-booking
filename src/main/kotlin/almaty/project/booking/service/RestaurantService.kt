package almaty.project.booking.service

import almaty.project.booking.commands.AddRestaurantCommand
import almaty.project.booking.domain.Restaurant
import almaty.project.booking.domain.ReviewDto
import almaty.project.booking.domain.TimeSlotDto
import almaty.project.booking.events.*
import almaty.project.booking.repository.RestaurantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class RestaurantService(private val restaurantRepository: RestaurantRepository, private val eventProducer: EventProducer) {
    fun getAllRestaurants(): Flux<Restaurant> = restaurantRepository.findAll()

    fun getRestaurantById(id: String): Mono<Restaurant> = restaurantRepository.findById(id)

    fun createRestaurant(restaurant: Restaurant): Mono<Restaurant> = restaurantRepository.save(restaurant)

    fun updateRestaurant(id: String, restaurant: Restaurant): Mono<Restaurant> =
            restaurantRepository.findById(id)
                    .flatMap {
                        val updatedRestaurant = it.copy(
                                name = restaurant.name,
                                reviews = restaurant.reviews,
                                address = restaurant.address,
                                phone = restaurant.phone,
                                availableTimeSlots = restaurant.availableTimeSlots
                        )
                        restaurantRepository.save(updatedRestaurant)
                    }

    fun deleteRestaurantById(id: String): Mono<Void> = restaurantRepository.deleteById(id)

    fun addRestaurant(command: AddRestaurantCommand): Mono<Restaurant> {
        return command.toRestaurantEntity()
                .flatMap { restaurantRepository.save(it) }
                .doOnSuccess { eventProducer.sendRestaurantAddedEvent(it) }
    }

    fun addReview(event: ReviewAddedEvent): Mono<Restaurant?> {
        val review = ReviewDto(event.reviewId, event.userId, event.rating, event.reviewText)
        return getRestaurantById(event.restaurantId)
                .flatMap { restaurant ->
                    val updatedReviews = restaurant.reviews.orEmpty().toMutableList().apply {
                        add(review)
                    }
                    val updatedRestaurant = restaurant.copy(reviews = updatedReviews)

                    logger.info("Saving $updatedRestaurant to database")
                    restaurantRepository.save(updatedRestaurant)
                            .doOnSuccess {
                                logger.info("Saved $it to database")
                            }
                }
    }

    fun editReview(event: ReviewEditedEvent): Mono<Restaurant?> {
        return restaurantRepository.findById(event.restaurantId)
                .flatMap { restaurant ->
                    val updatedReviews = restaurant.reviews?.map { review ->
                        if (review.userId == event.userId && review.reviewId == event.reviewId) {
                            review.copy(rating = event.newRating, comment = event.newComment)
                        } else {
                            review
                        }
                    }?.toMutableList()
                    val updatedRestaurant = restaurant.copy(reviews = updatedReviews)
                    restaurantRepository.save(updatedRestaurant).doOnSuccess {
                        logger.info("Saved updated $it to database")
                    }
                }
    }

    fun deleteReview(event: ReviewDeletedEvent): Mono<Restaurant> {
        return restaurantRepository.findById(event.restaurantId)
                .flatMap { restaurant ->
                    val updatedReviews = restaurant.reviews?.apply {
                        removeIf {it.reviewId == event.reviewId}
                    }
                    val updatedRestaurant = restaurant.copy(reviews = updatedReviews)
                    restaurantRepository.save(updatedRestaurant).doOnSuccess {
                        logger.info("Saved updated $it to database")
                    }
        }
    }

    fun addTimeSlot(event: TimeSlotAddedEvent): Mono<Restaurant> {
        return restaurantRepository.findById(event.restaurantId).flatMap { restaurant ->
            val updatedTimeSlots = restaurant.availableTimeSlots?.toMutableList() ?: mutableListOf()

            updatedTimeSlots.apply {
                add(TimeSlotDto(event.timeSlotId,event.time,event.isAvailable!!))
            }
            val updatedRestaurant = restaurant.copy(availableTimeSlots = updatedTimeSlots)
            restaurantRepository.save(updatedRestaurant).doOnSuccess {
                logger.info("Saved updated $it to database")
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}