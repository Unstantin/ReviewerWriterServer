package com.reviewerwriter.controllers

import com.reviewerwriter.*
import com.reviewerwriter.dto.requests.ActionToAnotherAccountRequest
import com.reviewerwriter.dto.response.AccountInfoPrivate
import com.reviewerwriter.dto.response.AccountInfoPublic
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.services.AccountService
import com.reviewerwriter.services.JwtService
import com.reviewerwriter.services.LogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.net.URL
import java.time.LocalDateTime

@Controller
@RequestMapping("/v1/accounts")
class AccountController(val accountService: AccountService, val jwtService: JwtService, val logService: LogService) {
    @Operation(summary = "Получение приватной информации об аккаунте")
    @GetMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(schema = Schema(implementation = AccountInfoPrivate::class)) ])
    ])
    fun getPrivateInfo(servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAccountInfoPrivate()
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Редактирование информации об аккаунте", description = "Доступные поля: nickname, tags")
    @PatchMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message) ]
    )
    fun updateAccountInfo(@RequestBody fields: Map<String, Any>, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.updateAccountInfo(fields)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(OK_message)
        }

        logService.createLog(requestDateTime=requestDateTime, fields, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение всех рецензий своего аккаунта")
    @GetMapping("/reviews")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(array = ArraySchema(schema = Schema(implementation = ReviewInfo::class))) ])
    ])
    fun getAllAccountReviews(servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAllAccountReviews()
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение всех избранных рецензий")
    @GetMapping("/reviews/favorites")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(array = ArraySchema(schema= Schema(implementation = ReviewInfo::class))) ])
    ])
    fun getAllFavoriteReviews(servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAllFavoriteReviews()

        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение всех подписчиков своего аккаунта")
    @GetMapping("/followers")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(array = ArraySchema(schema = Schema(implementation = AccountInfoPublic::class))) ])
    ])
    fun getAllFollowers(servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAllFollow(isFollowers = true)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение всех подписок своего аккаунта")
    @GetMapping("/following")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(array = ArraySchema(schema = Schema(implementation = AccountInfoPublic::class))) ])
    ])
    fun getAllFollowing(servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAllFollow(isFollowers = false)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение публичной информации аккаунта")
    @GetMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = ACCOUNT_NOT_FOUND_code, description = ACCOUNT_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(schema = Schema(implementation = AccountInfoPublic::class)) ])
    ])
    fun getAccountInfoPublic(@PathVariable id: Int, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAccountInfoPublic(id)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }


    @Operation(summary = "Действие по отношению к другому аккаунту", description = "Поле action может принимать значения: TO_FOLLOW")
    @PostMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = ACCOUNT_NOT_FOUND_code, description = ACCOUNT_NOT_FOUND_message),
        ApiResponse(responseCode = ATTEMPT_FOLLOW_YOURSELF_code, description = ATTEMPT_FOLLOW_YOURSELF_message),
        ApiResponse(responseCode = OK_code, description = OK_message)
    ])
    fun makeActionToAnotherAccount(@PathVariable id: Int, @RequestBody request: ActionToAnotherAccountRequest, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()

        val result = when(ActionToAnotherAccount.valueOf(request.action.uppercase())) {
            ActionToAnotherAccount.TO_FOLLOW -> accountService.followToAccount(id)
        }

        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение информации о подписчиках другого аккаунта")
    @GetMapping("/{id}/followers")
    @ApiResponses(value = [
        ApiResponse(responseCode = ACCOUNT_NOT_FOUND_code, description = ACCOUNT_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(array = ArraySchema(schema= Schema(implementation = AccountInfoPublic::class))) ])
    ])
    fun getAllFollowersOfAnotherAccount(@PathVariable id: Int, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAllFollow(isFollowers = true, isMyAccount = false, accountId = id)

        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение информации о подписчиках другого аккаунта")
    @GetMapping("/{id}/following")
    @ApiResponses(value = [
        ApiResponse(responseCode = ACCOUNT_NOT_FOUND_code, description = ACCOUNT_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(array = ArraySchema(schema= Schema(implementation = AccountInfoPublic::class))) ])
    ])
    fun getAllFollowingOfAnotherAccount(@PathVariable id: Int, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAllFollow(isFollowers = false, isMyAccount = false, accountId = id)

        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение всех рецензий другого аккаунта")
    @GetMapping("/{id}/reviews")
    @ApiResponses(value = [
        ApiResponse(responseCode = ACCOUNT_NOT_FOUND_code, description = ACCOUNT_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(array = ArraySchema(schema= Schema(implementation = ReviewInfo::class))) ])
    ])
    fun getAllReviewsOfAnotherAccount(@PathVariable id: Int, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accountService.getAllAccountReviews(id, isMyAccount = false)

        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }
}