package com.edsonctm.auth.controller

import com.edsonctm.auth.constants.Messages
import com.edsonctm.auth.dto.CadastroDTO
import com.edsonctm.auth.dto.LoginDTO
import com.edsonctm.auth.models.User
import com.edsonctm.auth.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class AuthController(
    private val userService: UserService,
    private val messages: Messages
) {

    @GetMapping
    fun hello(): String{
        return "App rodando."
    }

    @PostMapping("signin")
    fun signIn(@RequestBody body: CadastroDTO): ResponseEntity<Any> {
        val user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password

        return ResponseEntity.ok(userService.save(user))
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO): ResponseEntity<Any>{
        val user = userService.findByEmail(body.email) ?:
        return ResponseEntity.badRequest().body(messages.USER_NOT_FIND)

        check(user.passwordValidator(body.password)) {
            return ResponseEntity.badRequest().body(messages.INVALID_PASSWORD)
        }

        return ResponseEntity.ok(user)
    }
}