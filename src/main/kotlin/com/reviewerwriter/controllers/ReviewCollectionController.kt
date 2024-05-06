package com.reviewerwriter.controllers

import com.reviewerwriter.*
import com.reviewerwriter.dto.requests.AddOrRemoveReviewToCollectionRequest
import com.reviewerwriter.dto.requests.ReviewCollectionCreateRequest
import com.reviewerwriter.dto.requests.ReviewCollectionCreateResponse
import com.reviewerwriter.services.JwtService
import com.reviewerwriter.services.LogService
import com.reviewerwriter.services.ReviewCollectionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.net.URL
import java.time.LocalDateTime

@Controller
@RequestMapping("/collections")
class ReviewCollectionController(
    val reviewCollectionService: ReviewCollectionService,
    val jwtService: JwtService,
    val logService: LogService
) {

    @Operation(description = "Создание коллекции рецензий")
    @PostMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message, content = [
            Content(schema = Schema(implementation = ReviewCollectionCreateResponse::class))
        ]),
    ])
    fun createReviewCollection(request: ReviewCollectionCreateRequest,servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewCollectionService.createReviewCollection(request)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(description = "Добавление рецензии к коллекции или удаление из нее")
    @PostMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = REVIEW_NOT_FOUND_code, description = "$REVIEW_NOT_FOUND_message или $REVIEW_COLLECTION_NOT_FOUND_message"),
        ApiResponse(responseCode = ACCESS_IS_DENIED_code, description = ACCESS_IS_DENIED_message),
        ApiResponse(responseCode = OK_code, description = OK_message)
    ])
    fun addOrRemoveReviewToCollection(@PathVariable id: Int, @RequestBody request: AddOrRemoveReviewToCollectionRequest, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewCollectionService.addOrRemoveReviewToCollection(id, request)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(description = "Удаление коллекции рецензий")
    @DeleteMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = REVIEW_COLLECTION_NOT_FOUND_code, description = REVIEW_COLLECTION_NOT_FOUND_message),
        ApiResponse(responseCode = ACCESS_IS_DENIED_code, description = ACCESS_IS_DENIED_message),
        ApiResponse(responseCode = OK_code, description = OK_message)
    ])
    fun deleteReviewCollection(@PathVariable id: Int, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewCollectionService.deleteReviewCollection(id)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Редактирование информации о коллекции", description = "Доступные поля: name")
    @PatchMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = REVIEW_COLLECTION_NOT_FOUND_code, description = REVIEW_COLLECTION_NOT_FOUND_message),
        ApiResponse(responseCode = ACCESS_IS_DENIED_code, description = ACCESS_IS_DENIED_message),
        ApiResponse(responseCode = OK_code, description = OK_message)
    ])
    fun updateReviewCollectionInfo(@PathVariable id: Int, @RequestBody fields: Map<String, Any>, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewCollectionService.updateReviewCollectionInfo(id, fields)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, fields, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }
}