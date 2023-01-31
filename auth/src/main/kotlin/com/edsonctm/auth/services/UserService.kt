package com.edsonctm.auth.services

import com.edsonctm.auth.models.User
import com.edsonctm.auth.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
){

    fun save(user: User): User{
        return userRepository.save(user)
    }

    fun findByEmail(email: String): User?{
        return userRepository.findByEmail(email)
    }

    fun getById(id: Int): Optional<User> {
        return userRepository.findById(id)
    }
}