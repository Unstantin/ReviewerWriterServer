package com.reviewerwriter

enum class ErrorMessages(val message: String, val code: Int) {
    USER_FROM_TOKEN_NOT_FOUND("Пользователь из токена не найден", 404),
    ACCESS_IS_DENIED("У Вас нет доступа к данному контенту", 403),
    REVIEW_NOT_FOUND("Рецензия не найдена", 404),
    ACCOUNT_NOT_FOUND("Аккаунт не найден", 404),
    USERNAME_IS_ALREADY_TAKEN("Это имя пользователя уже занято", 400),
    ERROR_CREATING_TOKEN("Ошибка создания токена", 400)
}