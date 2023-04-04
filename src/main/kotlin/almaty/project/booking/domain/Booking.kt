package almaty.project.booking.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "booking")
data class Booking(
        @Id
        val id: Long? = null,
        @Column("user_id")
        val userId: Long,
        @Column("restaurant_id")
        val restaurantId: String,
        @Column("time_slot_id")
        var timeSlotId: Long,
        @Column("status")
        var status: String,
        @Column("party_size")
        var partySize: Int,
        @Column("special_request")
        var specialRequest: String?=null
)
