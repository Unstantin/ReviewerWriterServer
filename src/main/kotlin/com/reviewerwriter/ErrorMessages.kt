package com.reviewerwriter

enum class ErrorMessages(var message: String, val code: Int) {
    TOKEN_ERROR(
        TOKEN_ERROR_message,
        TOKEN_ERROR_code.toInt()),
    REVIEW_NOT_FOUND(
        REVIEW_NOT_FOUND_message,
        REVIEW_NOT_FOUND_code.toInt()),
    ACCOUNT_NOT_FOUND(
        ACCOUNT_NOT_FOUND_message,
        ACCOUNT_NOT_FOUND_code.toInt()),
    USERNAME_IS_ALREADY_TAKEN(
        USERNAME_IS_ALREADY_TAKEN_message,
        USERNAME_IS_ALREADY_TAKEN_code.toInt()),
    NICKNAME_IS_ALREADY_TAKEN(
        NICKNAME_IS_ALREADY_TAKEN_message,
        NICKNAME_IS_ALREADY_TAKEN_code.toInt()
    ),
    ERROR_CREATING_TOKEN(
        ERROR_CREATING_TOKEN_message,
        ERROR_CREATING_TOKEN_code.toInt()),
    ACCESS_IS_DENIED(
        ACCESS_IS_DENIED_message,
        ACCESS_IS_DENIED_code.toInt()),
    ATTEMPT_FOLLOW_YOURSELF(
        ATTEMPT_FOLLOW_YOURSELF_message,
        ATTEMPT_FOLLOW_YOURSELF_code.toInt()
    )
}