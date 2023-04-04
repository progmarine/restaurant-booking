package almaty.project.booking.repository

import almaty.project.booking.domain.Booking
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : ReactiveCrudRepository<Booking, Long>