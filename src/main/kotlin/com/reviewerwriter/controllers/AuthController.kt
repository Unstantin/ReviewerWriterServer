package com.reviewerwriter.controllers

import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/auth")
class AuthController(val authService: AuthService) {
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/reg")
    fun registration(@RequestBody request: UserDTO): ResponseEntity<Any> {
        val res = authService.registration(request)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo!!.message)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }


    @Operation(summary = "Получение информации о пользователе")
    @PostMapping("/log")
    fun logIn(@RequestBody request: UserDTO): ResponseEntity<Any> {
        val res = authService.logIn(request)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo!!.message)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }
}