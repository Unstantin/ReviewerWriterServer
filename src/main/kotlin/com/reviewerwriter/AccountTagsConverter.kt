package com.reviewerwriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.reviewerwriter.models.AccountTags
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class AccountTagsConverter : AttributeConverter<AccountTags, String> {
    private val objectMapper : ObjectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: AccountTags?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): AccountTags {
        return objectMapper.readValue(dbData?.toByteArray(), AccountTags::class.java)
    }
}