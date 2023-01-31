package com.edsonctm.auth.models

import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.*

@Entity
@Table(name="jwt_user", schema="jwt_kotlin")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @Column
    var name = ""

    @Column
    var email = ""

    @JsonIgnore
    @Column
    var password = ""
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
    }

    fun passwordValidator(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }
}