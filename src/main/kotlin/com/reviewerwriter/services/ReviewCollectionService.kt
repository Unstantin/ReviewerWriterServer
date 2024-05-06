package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.requests.CollectionCreateRequest
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.repositories.AccessRoleRepository
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.ReviewCollectionRepository
import io.jsonwebtoken.Claims
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class ReviewCollectionService(
    val accountRepository: AccountRepository,
    val accessRoleRepository: AccessRoleRepository,
    val reviewCollectionRepository: ReviewCollectionRepository
) {
   /* fun createCollection(request: CollectionCreateRequest): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()


        }

        return info
    }*/
}