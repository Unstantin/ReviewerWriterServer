package com.reviewerwriter.dto.requests

data class UserCreateTagRequest(
    val userId: Int,
    val tagName: String,
    val criteria: String
)
