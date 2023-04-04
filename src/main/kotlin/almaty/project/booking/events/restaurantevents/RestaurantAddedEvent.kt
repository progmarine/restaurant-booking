package almaty.project.booking.events.restaurantevents

import almaty.project.booking.domain.ReviewDto
import almaty.project.booking.domain.TimeSlotDto
import almaty.project.booking.events.Event
import java.time.LocalDateTime

data class RestaurantAddedEvent(
        val name: String,
        val reviews: List<ReviewDto>? = null,
        val address: String,
        val phone: String? = null,
        val availableTimeSlots: List<TimeSlotDto>? = null,
        val createdAt: LocalDateTime = LocalDateTime.now()
) : Event