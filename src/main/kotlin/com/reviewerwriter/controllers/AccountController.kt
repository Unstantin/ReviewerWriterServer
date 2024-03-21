package com.reviewerwriter.controllers

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.services.AccountService
import com.reviewerwriter.services.JwtService
import io.jsonwebtoken.Claims
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/accounts")
class AccountController(val accountService: AccountService, val jwtService: JwtService) {
    @Operation(summary = "Получение приватной информации об аккаунте")
    @GetMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Аккаунт из токена не найден",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )
            ]
        ),
        ApiResponse(responseCode = "200", description = "ОК",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = AccountInfo::class)
                )
            ])
    ])
    fun getPrivateInfo() : ResponseEntity<Any> {
        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims

        val res = accountService.getPrivateAccountInfo(claims["accountId"] as Int)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

    @Operation(summary = "Создание пользовательского тега на аккаунте")
    @PostMapping("/tags")
    fun createAccountTag(@RequestBody request: AccountCreateTagRequest) : ResponseEntity<Any> {
        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims

        val res = accountService.createAccountTag(claims["accountId"] as Int, request)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }


    @Operation(summary = "Редактирование информации об аккаунте")
    @PatchMapping
    fun updateAccountInfo(@RequestBody fields: Map<String, Any>) : ResponseEntity<Any> {
        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims

        val res = accountService.updateAccountInfo(claims["accountId"] as Int, fields)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }
}