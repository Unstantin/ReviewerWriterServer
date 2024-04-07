package com.reviewerwriter.services

import com.google.gson.*
import com.reviewerwriter.entities.LogEntity
import com.reviewerwriter.repositories.LogRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class LogService(val logRepository: LogRepository) {
    var gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
    fun createLog(requestDateTime: LocalDateTime, request: Any?, response: ResponseEntity<Any>, username: String, method: String, endpoint: String) {
        logRepository.save(
            LogEntity(requestDateTime = requestDateTime, responseDateTime = LocalDateTime.now(),
                username = username, method = method, endpoint=endpoint,
                statusCode = response.statusCode.value(), requestBody = gson.toJson(request),
                responseBody = gson.toJson(response))
        )
    }
}

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")

    override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.parse(json.asString, formatter)
    }
}