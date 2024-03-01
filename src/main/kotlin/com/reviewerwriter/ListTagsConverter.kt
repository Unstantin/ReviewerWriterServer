package com.reviewerwriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.reviewerwriter.models.ListTags
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class ListTagsConverter : AttributeConverter<ListTags, String> {
    private val objectMapper : ObjectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: ListTags?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): ListTags {
        return objectMapper.readValue(dbData?.toByteArray(), ListTags::class.java)
    }
}