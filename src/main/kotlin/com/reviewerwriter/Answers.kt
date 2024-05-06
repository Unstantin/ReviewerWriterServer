package com.reviewerwriter

const val OK_code = "200"
const val OK_message = "OK"

const val TOKEN_ERROR_code = "401"
const val TOKEN_ERROR_message = "Некорректный токен"

const val REVIEW_NOT_FOUND_code = "404"
const val REVIEW_NOT_FOUND_message = "Рецензия не найдена"

const val ACCOUNT_NOT_FOUND_code = "404"
const val ACCOUNT_NOT_FOUND_message = "Аккаунт не найден"

const val ACCESS_ROLE_NOT_FOUND_code = "404"
const val ACCESS_ROLE_NOT_FOUND_message = "Роль доступа не найдена"

const val USERNAME_IS_ALREADY_TAKEN_code = "400"
const val USERNAME_IS_ALREADY_TAKEN_message = "Это имя пользователя уже занято"

const val NICKNAME_IS_ALREADY_TAKEN_code = "400"
const val NICKNAME_IS_ALREADY_TAKEN_message = "Этот никнейм уже занят"

const val NAME_OF_ACCESS_ROLE_IS_ALREADY_TAKEN_ON_THIS_ACCOUNT_code = "400"
const val NAME_OF_ACCESS_ROLE_IS_ALREADY_TAKEN_ON_THIS_ACCOUNT_message = "Имя группы доступа уже занято"

const val ERROR_CREATING_TOKEN_code = "400"
const val ERROR_CREATING_TOKEN_message = "Ошибка создания токена"

const val ACCESS_IS_DENIED_code = "403"
const val ACCESS_IS_DENIED_message = "У Вас нет прав на это действие"

const val ATTEMPT_FOLLOW_YOURSELF_code = "409"
const val ATTEMPT_FOLLOW_YOURSELF_message = "Вы не можете подписаться на самого себя"

const val ATTEMPT_TO_GIVE_ACCESS_ROLE_YOURSELF_code = "409"
const val ATTEMPT_TO_GIVE_ACCESS_ROLE_YOURSELF_message = "Вы не можете дать себе роль доступа"