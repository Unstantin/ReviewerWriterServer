package com.reviewerwriter.controllers

import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.dto.response.JwtInfo
import com.reviewerwriter.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    @ApiResponses(value = [
        ApiResponse(responseCode = "400", description = "Это имя пользователя уже занято"),
        ApiResponse(responseCode = "200", description = "ОК")
    ])
    fun registration(@RequestBody request: UserDTO): ResponseEntity<Any> {
        val res = authService.registration(request)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo!!.message)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }


    @Operation(summary = "Получение jwt токена")
    @PostMapping("/log")
    @ApiResponses(value = [
        ApiResponse(responseCode = "400", description = "Ошибка создания токена"),
        ApiResponse(responseCode = "200", description = "ОК",
            content = [ Content( schema = Schema(implementation = JwtInfo::class) ) ])
    ])
    fun logIn(@RequestBody request: UserDTO): ResponseEntity<Any> {
        val res = authService.logIn(request)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo!!.message)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }
}