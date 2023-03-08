package almaty.project.booking.events

import almaty.project.booking.commands.AddTimeSlotCommand
import almaty.project.booking.commands.DeleteReviewCommand
import almaty.project.booking.config.EventProducerConfig
import almaty.project.booking.config.EventProducerConfig.Companion.REVIEW_DELETED_TOPIC
import almaty.project.booking.config.EventProducerConfig.Companion.TIMESLOT_ADDED_TOPIC
import almaty.project.booking.domain.Restaurant
import almaty.project.booking.domain.Review
import almaty.project.booking.domain.TimeSlot
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EventProducer(private val eventProducerConfig: EventProducerConfig) {

    fun sendReviewAddedEvent(review: Review) {
        val event = ReviewAddedEvent(reviewId = review.id!!, restaurantId = review.restaurantId,
                userId = review.userId,
                reviewText = review.comment,
                rating = review.rating)
        val topic = EventProducerConfig.REVIEW_ADDED_TOPIC
        val record = ProducerRecord<String, Event>(topic, event)
        eventProducerConfig.kafkaTemplate().send(record)
        logger.info(
                "Publishing event: " +
                        "\n\tTopic=[$topic] \n" +
                        "\n\tEvent=[$event]"
        )
    }

    fun sendReviewEditedEvent(review: Review) {
        val event = ReviewEditedEvent(restaurantId = review.restaurantId, reviewId = review.id!!,
                userId = review.userId,
                newComment = review.comment,
                newRating = review.rating)
        val topic = EventProducerConfig.REVIEW_EDITED_TOPIC
        val record = ProducerRecord<String, Event>(topic, event)
        eventProducerConfig.kafkaTemplate().send(record)
        logger.info(
                "Publishing event: " +
                        "\n\tTopic=[$topic] \n" +
                        "\n\tEvent=[$event]"
        )
    }

    fun sendRestaurantAddedEvent(restaurant: Restaurant) {
        val event = RestaurantAddedEvent(
                name = restaurant.name,
                address = restaurant.address,
                phone = restaurant.phone,
                availableTimeSlots = restaurant.availableTimeSlots,
                reviews = restaurant.reviews
        )
        val topic = "restaurants-added"
        val record = ProducerRecord<String, Event>(topic, event)
        logger.info(
                "Publishing event: " +
                        "\n\tTopic=[$topic] \n" +
                        "\n\tEvent=[$event]"
        )
        eventProducerConfig.kafkaTemplate().send(record)
    }

    fun sendReviewDeletedEvent(command: DeleteReviewCommand) {
        val event = ReviewDeletedEvent(reviewId = command.reviewId, restaurantId = command.restaurantId)
        val topic = REVIEW_DELETED_TOPIC
        val record = ProducerRecord<String, Event>(topic, event)
        logger.info(
                "Publishing event: " +
                        "\n\tTopic=[$topic] \n" +
                        "\n\tEvent=[$event]"
        )
        eventProducerConfig.kafkaTemplate().send(record)
    }

    fun sendTimeSlotAddedEvent(command: AddTimeSlotCommand, timeSlot: TimeSlot) {
        val event = TimeSlotAddedEvent(timeSlot.id!!, command.restaurantId, command.time, timeSlot.isAvailable)
        val topic = TIMESLOT_ADDED_TOPIC
        val record = ProducerRecord<String, Event>(topic, event)
        logger.info(
                "Publishing event: " +
                        "\n\tTopic=[$topic] \n" +
                        "\n\tEvent=[$event]"
        )
        eventProducerConfig.kafkaTemplate().send(record)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
