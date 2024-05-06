package com.reviewerwriter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reviewerwriter.entities.ReviewCollectionEntity
import com.reviewerwriter.models.Tag
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class TagsConverter : AttributeConverter<ArrayList<Tag>, String> {
    private val gson = Gson()

    override fun convertToDatabaseColumn(attribute: ArrayList<Tag>?): String {
        return gson.toJson(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): ArrayList<Tag> {
        val typeToken = object : TypeToken<ArrayList<Tag>>() {}.type
        return gson.fromJson(dbData, typeToken)
    }
}
