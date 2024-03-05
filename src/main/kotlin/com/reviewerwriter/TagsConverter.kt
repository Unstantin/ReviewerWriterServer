package com.reviewerwriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.reviewerwriter.models.Tag
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class TagsConverter : AttributeConverter<ArrayList<Tag>, String> {
    private val objectMapper : ObjectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: ArrayList<Tag>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): ArrayList<Tag> {
        val containerClass: Class<*> = ArrayList::class.java
        val elementType: Class<*> = Tag::class.java
        val javaType = objectMapper.typeFactory.constructParametricType(containerClass, elementType)
        return objectMapper.readValue(dbData?.toByteArray(), javaType)
    }
}