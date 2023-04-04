package almaty.project.booking.service

import almaty.project.booking.commands.AddRestaurantCommand
import almaty.project.booking.commands.UpdateRestaurantCommand
import almaty.project.booking.domain.Restaurant
import almaty.project.booking.events.restaurantevents.RestaurantAddedEvent
import almaty.project.booking.events.restaurantevents.RestaurantUpdatedEvent
import almaty.project.booking.events.reviewevents.ReviewAddedEvent
import almaty.project.booking.events.reviewevents.ReviewDeletedEvent
import almaty.project.booking.events.reviewevents.ReviewEditedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotAddedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotUpdatedEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface RestaurantService {
    fun getAllRestaurants(): Flux<Restaurant>
    fun getRestaurantById(id: String): Mono<Restaurant>
    fun deleteRestaurantById(id: String): Mono<Void>
    fun addRestaurant(command: AddRestaurantCommand): Mono<RestaurantAddedEvent>
    fun updateRestaurant(command: UpdateRestaurantCommand): Mono<RestaurantUpdatedEvent>
    fun updateTimeSlot(event: TimeSlotUpdatedEvent): Mono<Restaurant?>
    fun addReview(event: ReviewAddedEvent): Mono<Restaurant?>
    fun editReview(event: ReviewEditedEvent): Mono<Restaurant?>
    fun deleteReview(event: ReviewDeletedEvent): Mono<Restaurant>
    fun addTimeSlot(event: TimeSlotAddedEvent): Mono<Restaurant>
}