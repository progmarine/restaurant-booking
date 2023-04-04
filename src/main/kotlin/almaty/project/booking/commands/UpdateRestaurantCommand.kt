package almaty.project.booking.commands

import almaty.project.booking.domain.ReviewDto
import almaty.project.booking.domain.TimeSlotDto

data class UpdateRestaurantCommand(
        val restaurantId: String,
        val timeSlotDto: TimeSlotDto? = null,
        val reviewDto: ReviewDto? = null
)