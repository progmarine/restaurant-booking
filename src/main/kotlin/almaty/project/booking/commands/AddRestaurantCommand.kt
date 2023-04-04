package almaty.project.booking.commands

import almaty.project.booking.domain.Restaurant
import almaty.project.booking.domain.TimeSlotDto
import reactor.core.publisher.Mono

data class AddRestaurantCommand(
        val name: String,
        val address: String,
        val phone: String,
        val availableTimeSlots: List<TimeSlotDto>?=null
) {
    fun toRestaurantEntity(): Mono<Restaurant> {
        return Mono.just(Restaurant(
                name = this.name,
                address = this.address,
                phone = this.phone,
                availableTimeSlots = this.availableTimeSlots
        ))
    }
}