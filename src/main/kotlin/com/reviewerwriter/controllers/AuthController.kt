package com.reviewerwriter.controllers

import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.JwtInfo
import com.reviewerwriter.services.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(val authService: AuthService) {
    @PostMapping("/reg")
    fun registration(@RequestBody request: UserDTO): Info {
        return authService.registration(request)
    }

    @PostMapping("/log")
    fun logIn(@RequestBody request: UserDTO): JwtInfo {
        return authService.logIn(request)
    }
}