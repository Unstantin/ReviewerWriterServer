package com.reviewerwriter.controllers

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.requests.AccountInfoRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.services.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController(private val accountService: AccountService) {
    @GetMapping
    fun getInfo(@RequestBody request: AccountInfoRequest) : AccountInfo {
        return accountService.getAccountInfo(request.accountId)
    }

    @PostMapping("/tags")
    fun createTag(@RequestBody request: AccountCreateTagRequest) {
        accountService.createTag(request)
    }
}