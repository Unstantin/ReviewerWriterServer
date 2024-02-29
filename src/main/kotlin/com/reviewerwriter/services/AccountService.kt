package com.reviewerwriter.services

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.entities.UserEntity
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AccountService(
        val accountRepository: AccountRepository,
        val userRepository: UserRepository
) {
    fun getAccountInfo(accountId: Int) : AccountInfo {
        val info = AccountInfo()

        val auth = SecurityContextHolder.getContext().authentication
        val userDetailsFromAuth: UserDetails = auth.principal as UserEntity
        val userEntityFromAuth = userRepository.findByUsername(userDetailsFromAuth.username)

        if(userEntityFromAuth.isPresent) {
            if(userEntityFromAuth.get().accountEntity.id != accountId) {
                info.errorInfo = "У Вас нет доступа к этой странице"
                return info
            }
        } else {
            info.errorInfo = "Пользователь из аунтификации не найден"
            return info
        }

        val account: AccountEntity = accountRepository.getReferenceById(accountId)
        info.nickname = account.nickname
        info.tags = account.accountTags
        return info
    }

    fun createTag(userCreateTagRequest: AccountCreateTagRequest) {
        val account: AccountEntity = accountRepository.getReferenceById(userCreateTagRequest.accountId)
        account.addTag(userCreateTagRequest.tagName, userCreateTagRequest.criteria.split(","))
        accountRepository.save(account)
    }
}