package com.reviewerwriter.dto.requests

data class AccountCreateTagRequest(
    val accountId: Int,
    val tagName: String,
    val criteria: String
)
