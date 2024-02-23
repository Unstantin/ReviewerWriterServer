package com.reviewerwriter.controllers

import com.reviewerwriter.dto.requests.UserCreateTagRequest
import com.reviewerwriter.dto.requests.UserInfoRequest
import com.reviewerwriter.dto.requests.UserPutRequest
import com.reviewerwriter.dto.response.UserInfo
import com.reviewerwriter.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {
    @GetMapping
    fun getInfo(@RequestBody request: UserInfoRequest) : UserInfo {
        return userService.getUserInfo(request.userId)
    }

    @PostMapping
    fun putUser(@RequestBody request: UserPutRequest) {
        userService.putUser(request)
    }

    @PostMapping("/tags")
    fun createTag(@RequestBody request: UserCreateTagRequest) {
        userService.createTag(request)
    }

}