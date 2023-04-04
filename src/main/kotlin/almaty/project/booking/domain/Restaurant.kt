package almaty.project.booking.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.relational.core.mapping.Column

@Document(collection = "restaurants")
data class Restaurant(

//        @Transient val SEQUENCE_NAME: String = "restaurants_sequence",

        @Id
        @Indexed(unique=true)
        val id: String?=null,
        @Column("name")
        val name: String,
        @Column("reviews")
        var reviews: List<ReviewDto>? = null,
        @Column("address")
        val address: String,
        @Column("phone")
        val phone: String? = null,
        @Column("available_time_slots")
        val availableTimeSlots: List<TimeSlotDto>? = null
) {
    override fun toString(): String {
        return "$name $reviews $address $availableTimeSlots"
    }
}