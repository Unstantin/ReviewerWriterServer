package com.reviewerwriter.controllers

import com.reviewerwriter.dto.AccountDTO
import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.requests.AccountInfoRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.services.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/accounts")
class AccountController(val accountService: AccountService) {
    @GetMapping
    fun getInfo(@RequestBody request: AccountInfoRequest) : AccountInfo
        = accountService.getAccountInfo(request.accountId)


    @PostMapping("/tags")
    fun createTag(@RequestBody request: AccountCreateTagRequest)
        = accountService.createTag(request)


    @PutMapping
    fun updateAccountInfo(@RequestBody request: AccountDTO)
        = accountService.updateAccountInfo()
}