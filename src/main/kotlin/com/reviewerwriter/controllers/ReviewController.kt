package com.reviewerwriter.controllers

import com.reviewerwriter.dto.requests.ReviewCreateRequest
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.services.JwtService
import com.reviewerwriter.services.LogService
import com.reviewerwriter.services.ReviewService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.net.URL
import java.time.LocalDateTime

@RequestMapping("/v1/reviews")
@Controller
class ReviewController(
    val reviewService: ReviewService,
    val logService: LogService,
    val jwtService: JwtService
) {
    @Operation(summary = "Создание рецензии на аккаунте")
    @PostMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = "401", description = "Некорректный токен"),
        ApiResponse(responseCode = "200", description = "ОК")
    ])
    fun createReview(@RequestBody request: ReviewCreateRequest, servlet: HttpServletRequest): ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewService.createReview(request)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(200)).body("OK")
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение информации об рецензии")
    @GetMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Рецензия не найдена"),
        ApiResponse(responseCode = "200", description = "ОК",
            content = [ Content( schema = Schema(implementation = ReviewInfo::class) ) ]
        )]
    )
    fun getReviewInfo(@PathVariable id: Int, servlet: HttpServletRequest): ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val res = reviewService.getReviewInfoById(id)
        val response: ResponseEntity<Any> = if(res.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(res.errorInfo!!.code)).body(res.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(200)).body(res.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }
}