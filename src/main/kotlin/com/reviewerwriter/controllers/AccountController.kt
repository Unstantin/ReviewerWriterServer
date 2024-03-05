package com.reviewerwriter.controllers

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.services.AccountService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/accounts/{id}")
class AccountController(val accountService: AccountService) {
    @Operation(summary = "Получение информации об аккаунте")
    @GetMapping
    fun getInfo(@PathVariable id: Int) : AccountInfo
        = accountService.getAccountInfo(id)


    @Operation(summary = "Создание пользовательского тега на аккаунте")
    @PostMapping("/tags")
    fun createAccountTag(@PathVariable id: Int, @RequestBody request: AccountCreateTagRequest)
        = accountService.createAccountTag(id, request)

    @Operation(summary = "Обновление информации об аккаунте")
    @PatchMapping
    fun updateAccountInfo(@PathVariable id: Int, @RequestBody fields: Map<String, Any>)
        = accountService.updateAccountInfo(id, fields)
}