package com.edsonctm.auth.constants

import org.springframework.stereotype.Service

@Service
object Messages {
    val USER_NOT_FOUND = "Usuário não encontrado!"
    val INVALID_PASSWORD = "Senha inválida!"
    val SUCCESS = "ok!"
    val LOGOUT = "Usuário deslogado!"
}