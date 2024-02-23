package com.reviewerwriter.dto.response

data class UserInfo(
    val errorInfo: String = "No errors",
    val username: String,
    val tags: String
) {

}
