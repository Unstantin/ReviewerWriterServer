package com.reviewerwriter.services

import com.google.gson.*
import com.reviewerwriter.entities.LogEntity
import com.reviewerwriter.repositories.LogRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class LogService(val logRepository: LogRepository) {
    var gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
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

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString, formatter)
    }
}