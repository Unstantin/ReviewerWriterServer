package com.reviewerwriter.controllers

import com.reviewerwriter.dto.requests.ReviewCreateRequest
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.services.ReviewService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/review")
@RestController
class ReviewController(
    val reviewService: ReviewService
) {
    @PostMapping
    fun createReview(@RequestBody request: ReviewCreateRequest): Info {
        return reviewService.createReview(request)
    }
}