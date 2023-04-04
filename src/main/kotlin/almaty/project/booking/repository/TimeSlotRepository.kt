package almaty.project.booking.repository

import almaty.project.booking.domain.TimeSlot
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface TimeSlotRepository : R2dbcRepository<TimeSlot, Long> {
}