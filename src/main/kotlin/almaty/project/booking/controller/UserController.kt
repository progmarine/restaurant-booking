package almaty.project.booking.controller

import almaty.project.booking.domain.User
import almaty.project.booking.service.UserService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.findUserById(id)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/")
    suspend fun getAll(): ResponseEntity<Flow<User>> {
        val users = userService.findAll()
        return ResponseEntity.ok(users)
    }
}