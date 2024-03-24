package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
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
                info.errorInfo = ErrorMessages.ACCESS_IS_DENIED
                return info
            }
        } else {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
            return info
        }

        return info
    }
}