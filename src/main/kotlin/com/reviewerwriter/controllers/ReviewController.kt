package com.reviewerwriter.controllers

import com.reviewerwriter.*
import com.reviewerwriter.actions.ActionToAnotherAccountReview
import com.reviewerwriter.dto.requests.ActionToAnotherAccountReviewRequest
import com.reviewerwriter.dto.requests.GetFollowingReviewsRequest
import com.reviewerwriter.dto.requests.ReviewCreateRequest
import com.reviewerwriter.dto.response.ReviewCreateResponse
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
import org.springframework.web.bind.annotation.*
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
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content( schema = Schema(implementation = ReviewCreateResponse::class)) ])
    ])
    fun createReview(@RequestBody request: ReviewCreateRequest, servlet: HttpServletRequest): ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewService.createReview(request)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(OK_code.toInt())).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение рецензий от подписок")
    @GetMapping("/following")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content( schema = Schema(implementation = ReviewCreateResponse::class)) ])
    ])
    fun getFollowingReviews(@RequestBody request: GetFollowingReviewsRequest, servlet: HttpServletRequest): ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewService.getFollowingReviews(page=request.page)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(OK_code.toInt())).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение информации об рецензии")
    @GetMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = REVIEW_NOT_FOUND_code, description = REVIEW_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content( schema = Schema(implementation = ReviewInfo::class)) ])
    ])
    fun getReviewInfo(@PathVariable id: Int, servlet: HttpServletRequest): ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewService.getReviewInfoById(id)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(OK_code.toInt())).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Редактирование информации в рецензии", description = "Доступные поля: title, mainText, shortText, tags")
    @PatchMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = ACCESS_IS_DENIED_code, description = ACCESS_IS_DENIED_message),
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = REVIEW_NOT_FOUND_code, description = REVIEW_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message)]
    )
    fun editReviewData(@PathVariable id: Int, @RequestBody fields: Map<String, Any>, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewService.editReviewData(id, fields)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(OK_code.toInt())).body(OK_message)
        }

        logService.createLog(requestDateTime=requestDateTime, fields, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Удаление рецензии")
    @DeleteMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = ACCESS_IS_DENIED_code, description = ACCESS_IS_DENIED_message),
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = REVIEW_NOT_FOUND_code, description = REVIEW_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message)]
    )
    fun deleteReview(@PathVariable id: Int, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewService.deleteReview(id)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(OK_code.toInt())).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Действие по отношению к чужой рецензии", description = "Доступные значения поля action: TO_LIKE, TO_UNLIKE, TO_FAVORITE, TO_REMOVE_FAVORITE")
    @PostMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = REVIEW_NOT_FOUND_code, description = REVIEW_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message)]
    )
    fun actionToAnotherAccountReview(@PathVariable id: Int, @RequestBody request: ActionToAnotherAccountReviewRequest,
                                        servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewService.actionToReview(id, ActionToAnotherAccountReview.valueOf(request.action.uppercase()))
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(OK_code.toInt())).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }
}