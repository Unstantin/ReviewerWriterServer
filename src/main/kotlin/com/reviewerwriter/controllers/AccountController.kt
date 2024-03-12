package com.reviewerwriter.controllers

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.services.AccountService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/accounts/{id}")
class AccountController(val accountService: AccountService) {
    @Operation(summary = "Получение информации об аккаунте")
    @GetMapping
    fun getInfo(@PathVariable id: Int) : ResponseEntity<Any> {
        val res = accountService.getAccountInfo(id)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }



    @Operation(summary = "Создание пользовательского тега на аккаунте")
    @PostMapping("/tags")
    fun createAccountTag(@PathVariable id: Int, @RequestBody request: AccountCreateTagRequest) : ResponseEntity<Any> {
        val res = accountService.createAccountTag(id, request)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }


    @Operation(summary = "Обновление информации об аккаунте")
    @PatchMapping
    fun updateAccountInfo(@PathVariable id: Int, @RequestBody fields: Map<String, Any>) : ResponseEntity<Any> {
        val res = accountService.updateAccountInfo(id, fields)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }
}