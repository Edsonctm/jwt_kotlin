package com.edsonctm.auth.repository

import com.edsonctm.auth.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {

   fun findByEmail(email: String): User?
}