package almaty.project.booking.service

import almaty.project.booking.domain.User
import almaty.project.booking.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {
    suspend fun findUserById(id: Long): User? =
            repository.findById(id)

    suspend fun findAll(): Flow<User> = repository.findAll()
}