package almaty.project.booking.events.restaurantevents

import almaty.project.booking.domain.ReviewDto
import almaty.project.booking.domain.TimeSlotDto
import almaty.project.booking.events.Event

data class RestaurantUpdatedEvent(
        val restaurantId: String,
        val timeSlotDto: List<TimeSlotDto>?,
        val reviewDto: List<ReviewDto>?
) : Event