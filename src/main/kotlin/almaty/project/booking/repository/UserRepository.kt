package almaty.project.booking.repository

import almaty.project.booking.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long>