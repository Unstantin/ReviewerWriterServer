package com.reviewerwriter.services

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.repositories.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
        val accountRepository: AccountRepository,
        val accessService: AccessService
) {
    fun getAccountInfo(accountId: Int) : AccountInfo {
        val info = AccountInfo()

        val accessInfo = accessService.checkAccessToAccount(accountId).errorInfo
        if(accessInfo != null) {
            info.errorInfo = accessInfo
            return info
        }

        val account: AccountEntity = accountRepository.getReferenceById(accountId)
        info.nickname = account.nickname
        info.tags = account.tags
        return info
    }

    fun createTag(userCreateTagRequest: AccountCreateTagRequest) {
        val account: AccountEntity = accountRepository.getReferenceById(userCreateTagRequest.accountId)
        account.tags.addTag(userCreateTagRequest.tagName, userCreateTagRequest.criteria.split(","))
        accountRepository.save(account)
    }

    fun updateAccountInfo() {

    }
}