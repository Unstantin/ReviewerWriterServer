package com.reviewerwriter.services

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.repositories.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) {
    fun getAccountInfo(accountId: Int) : AccountInfo {
        val user: AccountEntity = accountRepository.getReferenceById(accountId)
        return AccountInfo(nickname = user.nickname, tags=user.accountTags)
    }

    fun createTag(userCreateTagRequest: AccountCreateTagRequest) {
        val account: AccountEntity = accountRepository.getReferenceById(userCreateTagRequest.accountId)
        account.addTag(userCreateTagRequest.tagName, userCreateTagRequest.criteria.split(","))
        accountRepository.save(account)
    }
}