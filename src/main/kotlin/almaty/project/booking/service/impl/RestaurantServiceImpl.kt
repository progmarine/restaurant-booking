package almaty.project.booking.service.impl

import almaty.project.booking.commands.AddRestaurantCommand
import almaty.project.booking.commands.UpdateRestaurantCommand
import almaty.project.booking.domain.Restaurant
import almaty.project.booking.domain.ReviewDto
import almaty.project.booking.domain.TimeSlotDto
import almaty.project.booking.events.producer.EventProducer
import almaty.project.booking.events.restaurantevents.RestaurantAddedEvent
import almaty.project.booking.events.restaurantevents.RestaurantUpdatedEvent
import almaty.project.booking.events.reviewevents.ReviewAddedEvent
import almaty.project.booking.events.reviewevents.ReviewDeletedEvent
import almaty.project.booking.events.reviewevents.ReviewEditedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotAddedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotUpdatedEvent
import almaty.project.booking.repository.RestaurantRepository
import almaty.project.booking.service.RestaurantService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class RestaurantServiceImpl(
        private val restaurantRepository: RestaurantRepository,
        private val eventProducer: EventProducer)
    : RestaurantService {

    override fun getAllRestaurants(): Flux<Restaurant> = restaurantRepository.findAll()

    override fun getRestaurantById(id: String): Mono<Restaurant> = restaurantRepository.findById(id)

    override fun deleteRestaurantById(id: String): Mono<Void> = restaurantRepository.deleteById(id)

    override fun addRestaurant(command: AddRestaurantCommand): Mono<RestaurantAddedEvent> {
        return command.toRestaurantEntity()
                .flatMap { restaurantRepository.save(it) }
                .flatMap { restaurant ->
                    val event = RestaurantAddedEvent(
                            name = restaurant.name,
                            address = restaurant.address,
                            phone = restaurant.phone,
                            availableTimeSlots = restaurant.availableTimeSlots,
                            reviews = restaurant.reviews
                    )
                    eventProducer.sendEvent(event)
                }
    }

    override fun updateRestaurant(command: UpdateRestaurantCommand): Mono<RestaurantUpdatedEvent> {
        return restaurantRepository.findById(command.restaurantId)
                .flatMap { restaurant ->
                    val updatedRestaurant = updateReviewsAndTimeSlots(restaurant, command.reviewDto, command.timeSlotDto)
                    restaurantRepository.save(updatedRestaurant)
                }
                .flatMap { restaurant ->
                    val event = RestaurantUpdatedEvent(
                            restaurantId = restaurant.id!!,
                            reviewDto = restaurant.reviews,
                            timeSlotDto = restaurant.availableTimeSlots
                    )
                    eventProducer.sendEvent(event)
                }
    }

    private fun updateReviewsAndTimeSlots(
            restaurant: Restaurant,
            reviewDto: ReviewDto?,
            timeSlotDto: TimeSlotDto?
    ): Restaurant {
        val updatedReviews = updateReviews(restaurant.reviews, reviewDto)
        val updatedTimeSlots = updateTimeSlots(restaurant.availableTimeSlots, timeSlotDto)
        return restaurant.copy(reviews = updatedReviews, availableTimeSlots = updatedTimeSlots)
    }

    private fun updateReviews(
            reviews: List<ReviewDto>?,
            reviewDto: ReviewDto?
    ): List<ReviewDto>? {
        if (reviewDto == null) return reviews

        val reviewToUpdate = reviews?.find { it.userId == reviewDto.userId && it.reviewId == reviewDto.reviewId }

        return if (reviewToUpdate != null) {
            reviews.map {
                if (it == reviewToUpdate) reviewToUpdate.copy(rating = reviewDto.rating, comment = reviewDto.comment)
                else it
            }
        } else {
            (reviews
                    ?: mutableListOf()).plus(ReviewDto(reviewId = reviewDto.reviewId, reviewDto.userId, reviewDto.rating, reviewDto.comment))
        }
    }

    private fun updateTimeSlots(
            timeSlots: List<TimeSlotDto>?,
            timeSlotDto: TimeSlotDto?
    ): List<TimeSlotDto>? {
        if (timeSlotDto == null) return timeSlots

        val timeSlotToUpdate = timeSlots?.find { it.timeslotId == timeSlotDto.timeslotId }

        return if (timeSlotToUpdate != null) {
            timeSlots.map {
                if (it == timeSlotToUpdate) timeSlotToUpdate.copy(time = timeSlotDto.time, partySize = timeSlotDto.partySize, isAvailable = timeSlotDto.isAvailable)
                else it
            }
        } else {
            (timeSlots
                    ?: mutableListOf()).plus(TimeSlotDto(timeSlotDto.timeslotId, timeSlotDto.time, timeSlotDto.partySize, timeSlotDto.isAvailable))
        }
    }

    override fun addReview(event: ReviewAddedEvent): Mono<Restaurant?> {
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

    override fun editReview(event: ReviewEditedEvent): Mono<Restaurant?> {
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

    override fun deleteReview(event: ReviewDeletedEvent): Mono<Restaurant> {
        return restaurantRepository.findById(event.restaurantId)
                .flatMap { restaurant ->
                    val updatedReviews = restaurant.reviews?.toMutableList()?.apply {
                        removeIf { it.reviewId == event.reviewId }
                    }
                    val updatedRestaurant = restaurant.copy(reviews = updatedReviews)
                    restaurantRepository.save(updatedRestaurant).doOnSuccess {
                        logger.info("Saved updated $it to database")
                    }
                }
    }

    override fun addTimeSlot(event: TimeSlotAddedEvent): Mono<Restaurant> {
        return restaurantRepository.findById(event.restaurantId).flatMap { restaurant ->
            val updatedTimeSlots = restaurant.availableTimeSlots?.toMutableList() ?: mutableListOf()

            updatedTimeSlots.apply {
                add(TimeSlotDto(event.timeSlotId, event.time, event.partySize, event.isAvailable!!))
            }
            val updatedRestaurant = restaurant.copy(availableTimeSlots = updatedTimeSlots)
            restaurantRepository.save(updatedRestaurant).doOnSuccess {
                logger.info("Saved updated $it to database")
            }
        }
    }

    override fun updateTimeSlot(event: TimeSlotUpdatedEvent): Mono<Restaurant?> {
        return restaurantRepository.findById(event.restaurantId)
                .flatMap { restaurant ->
                    val updatedTimeSlots = restaurant.availableTimeSlots?.map { timeSlot ->
                        if (timeSlot.timeslotId == event.timeSlotDto.timeslotId) {
                            timeSlot.copy(time = event.timeSlotDto.time, partySize = event.timeSlotDto.partySize, isAvailable = event.timeSlotDto.isAvailable)
                        } else {
                            timeSlot
                        }
                    }?.toMutableList()
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