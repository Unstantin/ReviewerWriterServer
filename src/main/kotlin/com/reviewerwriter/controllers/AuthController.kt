package com.reviewerwriter.controllers

import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.JwtInfo
import com.reviewerwriter.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(val authService: AuthService) {
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/reg")
    fun registration(@RequestBody request: UserDTO): Info
        = authService.registration(request)


    @Operation(summary = "Получение информации о пользователе")
    @PostMapping("/log")
    fun logIn(@RequestBody request: UserDTO): JwtInfo
        = authService.logIn(request)

}