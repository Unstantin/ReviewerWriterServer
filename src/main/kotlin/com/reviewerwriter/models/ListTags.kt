package com.reviewerwriter.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ListTags  (
    var tags: ArrayList<Tag> = ArrayList()
) {
    fun addTag(tagName: String, criteria: List<String>) {
        val list : ArrayList<Criteria> = ArrayList()
        for (criteriaName in criteria) {
            list.add(Criteria(criteriaName))
        }
        tags.add(Tag(name = tagName, criteria = list))
    }
}

data class Tag @JsonCreator constructor (
    @JsonProperty("name") val name: String,
    @JsonProperty("criteria") val criteria: ArrayList<Criteria>
)

data class Criteria @JsonCreator constructor (
    @JsonProperty("name") val name: String,
    @JsonProperty("value") var value: Int? = null
)