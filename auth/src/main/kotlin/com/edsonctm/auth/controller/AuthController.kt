package com.edsonctm.auth.controller

import com.edsonctm.auth.constants.Messages
import com.edsonctm.auth.dto.CadastroDTO
import com.edsonctm.auth.dto.LoginDTO
import com.edsonctm.auth.models.User
import com.edsonctm.auth.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

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
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any>{
        val user = userService.findByEmail(body.email) ?:
        return ResponseEntity.badRequest().body(messages.USER_NOT_FOUND)

        check(user.passwordValidator(body.password)) {
            return ResponseEntity.badRequest().body(messages.INVALID_PASSWORD)
        }

        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
            .signWith(SignatureAlgorithm.HS256, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(jwt)
    }

    @GetMapping("user")
    fun checkUser(@CookieValue("jwt") jwt: String?): ResponseEntity<Any>{
        try {
            if(jwt == null) return ResponseEntity.status(401).body(Messages.USER_NOT_FOUND)
            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
            val user = userService.getById(body.issuer.toInt())
            return ResponseEntity.ok(user)
        } catch (e: Exception) {
            return ResponseEntity.status(401).body(Messages.USER_NOT_FOUND)
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any>{
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Messages.LOGOUT)
    }
}