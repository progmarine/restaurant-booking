package almaty.project.booking.controller

import almaty.project.booking.commands.AddRestaurantCommand
import almaty.project.booking.domain.Restaurant
import almaty.project.booking.events.restaurantevents.RestaurantAddedEvent
import almaty.project.booking.service.RestaurantService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/restaurants")
class RestaurantController(private val restaurantService: RestaurantService) {

    @GetMapping("/{restaurantId}")
    fun getRestaurantById(@PathVariable restaurantId: String): Mono<Restaurant> {
        return restaurantService.getRestaurantById(restaurantId)
    }

    @GetMapping
    fun getAllRestaurants(): Flux<Restaurant> {
        return restaurantService.getAllRestaurants()
    }

    @PostMapping
    fun addRestaurant(@RequestBody command: AddRestaurantCommand): Mono<RestaurantAddedEvent> {
        return restaurantService.addRestaurant(command)
    }

    @DeleteMapping("/{restaurantId}")
    fun deleteRestaurant(@PathVariable restaurantId: String): Mono<Void> {
        return restaurantService.deleteRestaurantById(restaurantId)
    }
}