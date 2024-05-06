package com.reviewerwriter.controllers

import com.reviewerwriter.*
import com.reviewerwriter.dto.requests.CollectionCreateRequest
import com.reviewerwriter.services.AccountService
import com.reviewerwriter.services.JwtService
import com.reviewerwriter.services.LogService
import com.reviewerwriter.services.ReviewCollectionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URL
import java.time.LocalDateTime

@Controller
@RequestMapping("/v1/collections")
class ReviewCollectionController(
    val logService: LogService,
    val jwtService: JwtService,
    val reviewCollectionService: ReviewCollectionService
) {
   /* @Operation(summary = "Создание коллекции")
    @PostMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = NICKNAME_IS_ALREADY_TAKEN_code, description = NICKNAME_IS_ALREADY_TAKEN_message),
        ApiResponse(responseCode = OK_code, description = OK_message) ]
    )
    fun updateAccountInfo(@RequestBody request: CollectionCreateRequest, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = reviewCollectionService.createCollection(request)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(OK_message)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }*/
}