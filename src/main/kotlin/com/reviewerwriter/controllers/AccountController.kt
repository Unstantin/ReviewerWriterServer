package com.reviewerwriter.controllers

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.services.AccountService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/accounts")
class AccountController(val accountService: AccountService) {
    @Operation(summary = "Получение приватной информации об аккаунте")
    @GetMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Аккаунт из токена не найден"),
        ApiResponse(responseCode = "200", description = "ОК",
            content = [ Content( schema = Schema(implementation = AccountInfo::class) ) ])
    ])
    fun getPrivateInfo() : ResponseEntity<Any> {
        val res = accountService.getPrivateAccountInfo()
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

    @Operation(summary = "Создание пользовательского тега на аккаунте")
    @PostMapping("/tags")
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Аккаунт из токена не найден"),
        ApiResponse(responseCode = "202", description = "OK") ]
    )
    fun createAccountTag(@RequestBody request: AccountCreateTagRequest) : ResponseEntity<Any> {
        val res = accountService.createAccountTag(request)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }


    @Operation(summary = "Редактирование информации об аккаунте")
    @PatchMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Аккаунт из токена не найден"),
        ApiResponse(responseCode = "202", description = "OK") ]
    )
    fun updateAccountInfo(@RequestBody fields: Map<String, Any>) : ResponseEntity<Any> {
        val res = accountService.updateAccountInfo(fields)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }

    @Operation(summary = "Получение всех рецензий аккаунта")
    @GetMapping("/reviews")
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Аккаунт из токена не найден"),
        ApiResponse(responseCode = "200", description = "OK", content = [
            Content(schema = Schema(implementation = Array<ReviewInfo>::class))
        ])
    ])
    fun getAllAccountReviews() : ResponseEntity<Any> {
        val res = accountService.getAllAccountReviews()
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

}