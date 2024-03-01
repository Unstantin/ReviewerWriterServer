package com.reviewerwriter.services

import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.entities.UserEntity
import com.reviewerwriter.repositories.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AccessService(
    val userRepository: UserRepository
) {
    fun checkAccessToAccount(accountId: Int) : Info {
        val info = Info()
        val auth = SecurityContextHolder.getContext().authentication
        val userDetailsFromAuth: UserDetails = auth.principal as UserEntity
        val userEntityFromAuth = userRepository.findByUsername(userDetailsFromAuth.username)

        if(userEntityFromAuth.isPresent) {
            if(userEntityFromAuth.get().account.id != accountId) {
                info.errorInfo = "У Вас нет доступа к этой странице"
                return info
            }
        } else {
            info.errorInfo = "Пользователь из аунтификации не найден"
            return info
        }

        return info
    }
}