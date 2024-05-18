package com.reviewerwriter.controllers

import com.reviewerwriter.*
import com.reviewerwriter.dto.response.PhotoUploadResponse
import com.reviewerwriter.services.FileSystemStorageService
import com.reviewerwriter.services.JwtService
import com.reviewerwriter.services.LogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import java.time.LocalDateTime

@Controller
@RequestMapping("/photos")
class PhotoController(
    val storageService: FileSystemStorageService,
    val logService: LogService,
    val jwtService: JwtService
) {
    @Operation(summary = "Загрузка фото на сервер")
    @PostMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = FILE_IS_EMPTY_code, description = FILE_IS_EMPTY_message),
        ApiResponse(responseCode = OK_code, description = OK_message, content = [
            Content(schema = Schema(implementation = PhotoUploadResponse::class))
        ])
    ])
    fun uploadPhoto(@RequestParam("file") file: MultipartFile, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = storageService.store(file)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(OK_code.toInt())).body(OK_message)
        }

        logService.createLog(requestDateTime=requestDateTime, file, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

    @Operation(summary = "Получение изображения")
    @GetMapping("/{filename}")
    @ApiResponses(value = [
        ApiResponse(responseCode = ERROR_READING_FILE_code, description = ERROR_CREATING_TOKEN_message),
        ApiResponse(responseCode = OK_code, description = OK_message, content = [
            Content(schema = Schema(implementation = UrlResource::class))
        ])
    ])
    fun loadPhoto(@PathVariable filename: String, servlet: HttpServletRequest) : ResponseEntity<Any> {
        val requestDateTime = LocalDateTime.now()
        val result = storageService.loadAsResource(filename)
        val response: ResponseEntity<Any> = if(result.errorInfo != null) {
            ResponseEntity.status(HttpStatusCode.valueOf(result.errorInfo!!.code)).body(result.errorInfo)
        } else {
            ResponseEntity.status(HttpStatusCode.valueOf(OK_code.toInt())).body(OK_message)
        }

        logService.createLog(requestDateTime=requestDateTime, filename, response, jwtService.getUsernameFromToken(),
            method=servlet.method, endpoint = URL(servlet.requestURL.toString()).path)
        return response
    }

}