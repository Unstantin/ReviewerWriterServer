package com.reviewerwriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.reviewerwriter.models.UserTags
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class UserTagListConverter : AttributeConverter<UserTags, String> {
    private val objectMapper : ObjectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: UserTags?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): UserTags {
        return objectMapper.readValue(dbData?.toByteArray(), UserTags::class.java)
    }
}