package com.reviewerwriter.controllers

import com.reviewerwriter.ActionToAnotherAccount
import com.reviewerwriter.dto.requests.ActionToAnotherAccountRequest
import com.reviewerwriter.dto.response.AccountInfoPrivate
import com.reviewerwriter.dto.response.AccountInfoPublic
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.services.AccountService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/accounts")
class AccountController(val accountService: AccountService) {
    @Operation(summary = "Получение приватной информации об аккаунте")
    @GetMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = "401", description = "Некорректный токен"),
        ApiResponse(responseCode = "200", description = "ОК",
            content = [ Content( schema = Schema(implementation = AccountInfoPrivate::class) ) ])
    ])
    fun getPrivateInfo() : ResponseEntity<Any> {
        val res = accountService.getAccountInfoPrivate()
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

/*

    @Operation(summary = "Создание пользовательского тега на аккаунте")
    @PostMapping("/tags")
    @ApiResponses(value = [
        ApiResponse(responseCode = "401", description = "Некорректный токен"),
        ApiResponse(responseCode = "202", description = "OK") ]
    )
    fun createAccountTag(@RequestBody request: AccountCreateTagRequest) : ResponseEntity<Any> {
        val res = accountService.createAccountTag(request)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }
*/

    @Operation(summary = "Редактирование информации об аккаунте")
    @PatchMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = "401", description = "Некорректный токен"),
        ApiResponse(responseCode = "202", description = "OK") ]
    )
    fun updateAccountInfo(@RequestBody fields: Map<String, Any>) : ResponseEntity<Any> {
        val res = accountService.updateAccountInfo(fields)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body("OK")
        }
    }

    @Operation(summary = "Получение всех рецензий аккаунта")
    @GetMapping("/reviews")
    @ApiResponses(value = [
        ApiResponse(responseCode = "401", description = "Некорректный токен"),
        ApiResponse(responseCode = "200", description = "OK", content = [
            Content(schema = Schema(implementation = Array<ReviewInfo>::class))
        ])
    ])
    fun getAllAccountReviews() : ResponseEntity<Any> {
        val res = accountService.getAllAccountReviews()
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

    @Operation(summary = "Получение всех подписчиков своего аккаунта")
    @GetMapping("/followers")
    @ApiResponses(value = [
        ApiResponse(responseCode = "401", description = "Некорректный токен"),
        ApiResponse(responseCode = "200", description = "ОК", content = [
            Content(schema = Schema(implementation = Array<AccountInfoPublic>::class))
        ])
    ])
    fun getAllFollowers() : ResponseEntity<Any> {
        val res = accountService.getAllFollow(isFollowers = true)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

    @Operation(summary = "Получение всех подписок своего аккаунта")
    @GetMapping("/following")
    @ApiResponses(value = [
        ApiResponse(responseCode = "401", description = "Некорректный токен"),
        ApiResponse(responseCode = "200", description = "ОК", content = [
            Content(schema = Schema(implementation = Array<AccountInfoPublic>::class))
        ])
    ])
    fun getAllFollowing() : ResponseEntity<Any> {
        val res = accountService.getAllFollow(isFollowers = false)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

    @Operation(summary = "Получение публичной информации аккаунта")
    @GetMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Аккаунт не найден"),
        ApiResponse(responseCode = "200", description = "ОК", content = [
            Content(schema = Schema(implementation = AccountInfoPublic::class))
        ])
    ])
    fun getAccountInfoPublic(@PathVariable id: Int) : ResponseEntity<Any> {
        val res = accountService.getAccountInfoPublic(id)
        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }


    @Operation(summary = "Действие по отношению к другому аккаунту", description = "Поле action может принимать два значения: TO_FOLLOW и TO_UNFOLLOW")
    @PostMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "401", description = "Некорректный токен"),
        ApiResponse(responseCode = "404", description = "Аккаунт не найден"),
        ApiResponse(responseCode = "200", description = "ОК", content = [
            Content(schema = Schema(implementation = String::class))
        ])
    ])
    fun makeActionToAnotherAccount(@PathVariable id: Int, @RequestBody request: ActionToAnotherAccountRequest) : ResponseEntity<Any> {
        val res = when(ActionToAnotherAccount.valueOf(request.action.uppercase())) {
            ActionToAnotherAccount.TO_FOLLOW -> {
                accountService.followToAccount(id, mode=true)
            }

            ActionToAnotherAccount.TO_UNFOLLOW -> {
                accountService.followToAccount(id, mode=false)
            }
        }

        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

    @Operation(summary = "Получение информации о подписчиках другого пользователя")
    @GetMapping("/{id}/followers")
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Аккаунт не найден"),
        ApiResponse(responseCode = "200", description = "ОК", content = [
            Content(schema = Schema(implementation = Array<AccountInfoPublic>::class))
        ])
    ])
    fun getAllFollowersOfAnotherAccount(@PathVariable id: Int) : ResponseEntity<Any> {
        val res = accountService.getAllFollow(isFollowers = true, isMyAccount = false, id = id)

        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }

    @Operation(summary = "Получение информации о подписчиках другого пользователя")
    @GetMapping("/{id}/following")
    @ApiResponses(value = [
        ApiResponse(responseCode = "404", description = "Аккаунт не найден"),
        ApiResponse(responseCode = "200", description = "ОК", content = [
            Content(schema = Schema(implementation = Array<AccountInfoPublic>::class))
        ])
    ])
    fun getAllFollowingOfAnotherAccount(@PathVariable id: Int) : ResponseEntity<Any> {
        val res = accountService.getAllFollow(isFollowers = false, isMyAccount = false, id = id)

        return if(res.errorInfo != null) {
            ResponseEntity.status(res.errorInfo!!.code).body(res.errorInfo)
        } else {
            ResponseEntity.status(200).body(res.response)
        }
    }
}