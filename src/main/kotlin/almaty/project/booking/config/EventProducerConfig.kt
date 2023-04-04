package almaty.project.booking.config

import almaty.project.booking.events.Event
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class EventProducerConfig {
    companion object {
        const val BOOTSTRAP_SERVERS = "localhost:9092"
        const val REVIEW_ADDED_TOPIC = "review-added"
        const val REVIEW_EDITED_TOPIC = "review-edited"
        const val REVIEW_DELETED_TOPIC = "review-deleted"
        const val TIMESLOT_ADDED_TOPIC = "timeslot-added"
        const val TIMESLOT_UPDATED_TOPIC = "timeslot-updated"
        const val BOOKING_CREATED_TOPIC = "booking-created"
        const val RESTAURANT_ADDED_TOPIC = "restaurants-added"
        const val BOOKING_CONFIRMED_TOPIC = "booking-confirmed"
    }

    @Bean
    fun producerFactory(): DefaultKafkaProducerFactory<String, Event> {
        val props = mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Event> {
        return KafkaTemplate(producerFactory())
    }

}