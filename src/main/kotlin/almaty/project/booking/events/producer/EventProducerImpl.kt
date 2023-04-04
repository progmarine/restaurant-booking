package almaty.project.booking.events.producer

import almaty.project.booking.config.EventProducerConfig
import almaty.project.booking.config.EventProducerConfig.Companion.BOOKING_CONFIRMED_TOPIC
import almaty.project.booking.config.EventProducerConfig.Companion.BOOKING_CREATED_TOPIC
import almaty.project.booking.config.EventProducerConfig.Companion.RESTAURANT_ADDED_TOPIC
import almaty.project.booking.config.EventProducerConfig.Companion.REVIEW_ADDED_TOPIC
import almaty.project.booking.config.EventProducerConfig.Companion.REVIEW_DELETED_TOPIC
import almaty.project.booking.config.EventProducerConfig.Companion.REVIEW_EDITED_TOPIC
import almaty.project.booking.config.EventProducerConfig.Companion.TIMESLOT_ADDED_TOPIC
import almaty.project.booking.config.EventProducerConfig.Companion.TIMESLOT_UPDATED_TOPIC
import almaty.project.booking.events.Event
import almaty.project.booking.events.bookingevents.BookingConfirmedEvent
import almaty.project.booking.events.bookingevents.BookingCreatedEvent
import almaty.project.booking.events.restaurantevents.RestaurantAddedEvent
import almaty.project.booking.events.reviewevents.ReviewAddedEvent
import almaty.project.booking.events.reviewevents.ReviewDeletedEvent
import almaty.project.booking.events.reviewevents.ReviewEditedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotAddedEvent
import almaty.project.booking.events.timeslotevents.TimeSlotUpdatedEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class EventProducerImpl(private val eventProducerConfig: EventProducerConfig) : EventProducer {

    override fun <T : Event> sendEvent(event: T): Mono<T> {
        val topic = getTopicByEvent(event)
        val record = ProducerRecord<String, Event>(topic, event)
        logger.info(
                "Publishing event: " +
                        "\n\tTopic=[$topic] \n" +
                        "\n\tEvent=[$event]"
        )
        eventProducerConfig.kafkaTemplate().send(record)
        return Mono.just(event)
    }

    private fun getTopicByEvent(event: Event): String {
        return when (event) {
            is BookingCreatedEvent -> BOOKING_CREATED_TOPIC
            is BookingConfirmedEvent -> BOOKING_CONFIRMED_TOPIC
            is ReviewAddedEvent -> REVIEW_ADDED_TOPIC
            is ReviewEditedEvent -> REVIEW_EDITED_TOPIC
            is ReviewDeletedEvent -> REVIEW_DELETED_TOPIC
            is TimeSlotAddedEvent -> TIMESLOT_ADDED_TOPIC
            is TimeSlotUpdatedEvent -> TIMESLOT_UPDATED_TOPIC
            is RestaurantAddedEvent -> RESTAURANT_ADDED_TOPIC
            else -> throw Exception("No topic found for $event")
        }
    }


    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
