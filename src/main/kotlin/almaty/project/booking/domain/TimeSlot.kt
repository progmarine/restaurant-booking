package almaty.project.booking.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("timeslots")
data class TimeSlot(
        @Id val id: Long? = null,
        val restaurantId: String? = null,
        var time: LocalDateTime? = null,
        var partySize: Int?=null,
        var isAvailable: Boolean? = null
)