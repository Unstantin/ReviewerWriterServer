package com.reviewerwriter.controllers

import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.response.JwtInfo
import com.reviewerwriter.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/auth")
class AuthController(val authService: AuthService) {
    val logger = LoggerFactory.getLogger(this.javaClass)

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/reg")
    @ApiResponses(value = [
        ApiResponse(responseCode = "400", description = "Это имя пользователя уже занято"),
        ApiResponse(responseCode = "200", description = "ОК")
    ])
    fun registration(@RequestBody request: UserDTO): ResponseEntity<Any> {
        logger.info("\u001b[0;36mGET REQUEST: registration\u001b[0m")
        val res = authService.registration(request)
        return if(res.errorInfo != null) {
            logger.info("\u001b[0;31mREQUEST ERROR: registration {${res.errorInfo}}\u001b[0m")
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo!!.message)
        } else {
            logger.info("\u001b[0;32mREQUEST OK: registration\u001b[0m")
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
        logger.info("\u001b[0;36mGET REQUEST: login\u001b[0m")
        val res = authService.logIn(request)
        return if(res.errorInfo != null) {
            logger.info("\u001b[0;31mREQUEST ERROR: login {${res.errorInfo}}\u001b[0m")
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo!!.message)
        } else {
            logger.info("\u001b[0;32mREQUEST OK: login\u001b[0m")
            ResponseEntity.status(200).body(res.response)
        }
    }
}