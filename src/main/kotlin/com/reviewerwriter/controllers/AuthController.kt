package com.reviewerwriter.controllers

import com.reviewerwriter.*
import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.response.JwtInfo
import com.reviewerwriter.dto.response.RegistrationInfo
import com.reviewerwriter.services.AuthService
import com.reviewerwriter.services.LogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.net.URL
import java.time.LocalDateTime

@Controller
@RequestMapping("/v1/auth")
class AuthController(val authService: AuthService, val logService: LogService) {
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/reg")
    @ApiResponses(value = [
        ApiResponse(responseCode = USERNAME_IS_ALREADY_TAKEN_code, description = USERNAME_IS_ALREADY_TAKEN_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [Content(schema = Schema(implementation = RegistrationInfo::class)) ])
    ])
    fun registration(@RequestBody request: UserDTO, servlet: HttpServletRequest): ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = authService.registration(request)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo!!.message)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, request.username,
                            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }


    @Operation(summary = "Получение jwt токена")
    @PostMapping("/log")
    @ApiResponses(value = [
        ApiResponse(responseCode = ERROR_CREATING_TOKEN_code, description = ERROR_CREATING_TOKEN_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [Content(schema = Schema(implementation = JwtInfo::class)) ])
    ])
    fun logIn(@RequestBody request: UserDTO, servlet: HttpServletRequest): ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = authService.logIn(request)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo!!.message)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, request.username,
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }
}