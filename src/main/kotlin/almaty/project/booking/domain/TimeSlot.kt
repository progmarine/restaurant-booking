package almaty.project.booking.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("timeslots")
data class TimeSlot(
        @Id val id: Long? = null,
        val restaurantId: String? = null,
        val time: LocalDateTime? = null,
        val isAvailable: Boolean? = null
)