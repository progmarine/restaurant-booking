package almaty.project.booking.service

import almaty.project.booking.commands.CreateBookingCommand
import almaty.project.booking.commands.UpdateBookingCommand
import almaty.project.booking.domain.Booking
import almaty.project.booking.events.bookingevents.BookingCreatedEvent
import reactor.core.publisher.Mono

interface BookingService  {
    fun createBooking(command: CreateBookingCommand): Mono<BookingCreatedEvent>
    fun updateBooking(command: UpdateBookingCommand): Mono<Booking>
}