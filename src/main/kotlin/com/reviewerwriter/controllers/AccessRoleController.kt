package com.reviewerwriter.controllers

import com.reviewerwriter.*
import com.reviewerwriter.dto.requests.AccessRoleCreateRequest
import com.reviewerwriter.dto.response.AccessRoleCreateResponse
import com.reviewerwriter.dto.response.AccessRoleInfo
import com.reviewerwriter.services.AccessRoleService
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
import org.springframework.web.bind.annotation.*
import java.net.URL
import java.time.LocalDateTime

@Controller
@RequestMapping("/v1/access-roles")
class AccessRoleController(
    val jwtService: JwtService,
    val logService: LogService,
    val accessRoleService: AccessRoleService
) {
    @Operation(summary = "Получение всех созданных ролей доступа своего аккаунта")
    @GetMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [ Content(array = ArraySchema(schema = Schema(implementation = AccessRoleInfo::class))) ])
    ])
    fun getAllCreatedAccessRoles(servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accessRoleService.getAllCreatedAccessRoles()
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, null, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Создание роли доступа на своем аккаунте")
    @PostMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = NAME_OF_ACCESS_ROLE_IS_ALREADY_TAKEN_ON_THIS_ACCOUNT_code,
                    description = NAME_OF_ACCESS_ROLE_IS_ALREADY_TAKEN_ON_THIS_ACCOUNT_message),
        ApiResponse(responseCode = OK_code, description = OK_message,
            content = [Content(schema = Schema(implementation = AccessRoleCreateResponse::class)) ])
    ])
    fun createAccessRole(@RequestBody request: AccessRoleCreateRequest, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accessRoleService.createAccessRole(request.name)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(result.errorInfo!!.code).body(result.errorInfo)
        } else {
            ResponseEntity.status(OK_code.toInt()).body(result.response)
        }

        logService.createLog(requestDateTime=requestDateTime, request, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Удаление роли доступа на своем аккаунте")
    @DeleteMapping("/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = TOKEN_ERROR_code, description = TOKEN_ERROR_message),
        ApiResponse(responseCode = ACCESS_IS_DENIED_code, description = ACCESS_IS_DENIED_message),
        ApiResponse(responseCode = ACCESS_ROLE_NOT_FOUND_code, description = ACCESS_ROLE_NOT_FOUND_message),
        ApiResponse(responseCode = OK_code, description = OK_message)
    ])
    fun deleteAccessRole(@PathVariable id: Int, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = accessRoleService.deleteAccessRole(id)
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