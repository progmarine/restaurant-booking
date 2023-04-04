package almaty.project.booking.controller

import almaty.project.booking.commands.CreateBookingCommand
import almaty.project.booking.commands.UpdateBookingCommand
import almaty.project.booking.domain.Booking
import almaty.project.booking.events.bookingevents.BookingCreatedEvent
import almaty.project.booking.service.BookingService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/booking")
class BookingController(private val bookingService: BookingService) {

    @PostMapping
    fun createBooking(@RequestBody command: CreateBookingCommand): Mono<BookingCreatedEvent> {
        return bookingService.createBooking(command)
    }

    @PutMapping
    fun updateBooking(@RequestBody command: UpdateBookingCommand): Mono<Booking> {
        return bookingService.updateBooking(command)
    }
}