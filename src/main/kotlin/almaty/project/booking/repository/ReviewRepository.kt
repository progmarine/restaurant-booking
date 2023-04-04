package almaty.project.booking.repository

import almaty.project.booking.domain.Review
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ReviewRepository : ReactiveCrudRepository<Review, Long> {
    fun findByUserId(userId: Long): Flux<Review>

    fun findByRestaurantId(restaurantId: String): Flux<Review>
}