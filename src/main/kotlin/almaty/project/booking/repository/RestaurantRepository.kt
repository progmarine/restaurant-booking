package almaty.project.booking.repository

import almaty.project.booking.domain.Restaurant
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : ReactiveMongoRepository<Restaurant, String>
