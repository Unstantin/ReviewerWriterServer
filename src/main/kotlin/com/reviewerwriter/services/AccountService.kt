package com.reviewerwriter.services

import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.models.Tag
import com.reviewerwriter.repositories.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field

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


    fun createAccountTag(accountId: Int, request: AccountCreateTagRequest) {
        val account: AccountEntity = accountRepository.getReferenceById(accountId)
        account.tags.add(Tag(request.tagName, request.criteria))
        accountRepository.save(account)
    }


    fun updateAccountInfo(accountId: Int, fields: Map<String, Any>): Info {
        val info = Info()

        val accessInfo = accessService.checkAccessToAccount(accountId).errorInfo
        if(accessInfo != null) {
            info.errorInfo = accessInfo
            return info
        }

        val account = accountRepository.getReferenceById(accountId)
        fields.forEach { (key, value) ->
            run {
                val field: Field = ReflectionUtils.findField(AccountEntity::class.java, key)!!
                field.trySetAccessible()
                ReflectionUtils.setField(field, account, value)
            }
        }

        accountRepository.save(account)
        return info
    }
}