package com.reviewerwriter.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AccountTags  (
    val tags: ArrayList<AccountTag> = ArrayList()
)

data class AccountTag @JsonCreator constructor (
    @JsonProperty("name") val name: String,
    @JsonProperty("criteria") val criteria: ArrayList<Criteria>
)

data class Criteria @JsonCreator constructor (
    @JsonProperty("name") val name: String
)