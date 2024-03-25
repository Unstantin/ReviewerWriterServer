package com.reviewerwriter.services

import com.google.gson.Gson
import com.reviewerwriter.entities.LogEntity
import com.reviewerwriter.repositories.LogRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LogService(val logRepository: LogRepository) {
    var gson = Gson()
    fun createLog(requestDateTime: LocalDateTime, request: Any?, response: ResponseEntity<Any>, username: String, method: String, endpoint: String) {
        logRepository.save(
            LogEntity(requestDateTime = requestDateTime, responseDateTime = LocalDateTime.now(),
                username = username, method = method, endpoint=endpoint,
                statusCode = response.statusCode.value(), requestBody = gson.toJson(request),
                responseBody = gson.toJson(response))
        )
    }
}