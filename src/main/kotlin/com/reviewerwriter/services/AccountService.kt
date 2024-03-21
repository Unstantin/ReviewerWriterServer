package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
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

    fun getPrivateAccountInfo(accountId: Int) : Info {
        val info = Info()
        val accountInfo = AccountInfo()

        val accountOptional = accountRepository.findById(accountId)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()

            info.errorInfo = accessService.checkAccessToAccount(account.id!!).errorInfo
            if(info.errorInfo != null) return info

            accountInfo.nickname = account.nickname
            accountInfo.tags = account.tags
            info.response = accountInfo
        }

        return info
    }


    fun createAccountTag(accountId: Int, request: AccountCreateTagRequest) : Info {
        val info = Info()

        val accountOptional = accountRepository.findById(accountId)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()

            info.errorInfo = accessService.checkAccessToAccount(account.id!!).errorInfo
            if(info.errorInfo != null) return info

            account.tags.add(Tag(request.tagName, request.criteria))
            accountRepository.save(account)
        }

        return info
    }


    fun updateAccountInfo(accountId: Int, fields: Map<String, Any>): Info {
        val info = Info()

        val accountOptional = accountRepository.findById(accountId)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()

            info.errorInfo = accessService.checkAccessToAccount(account.id!!).errorInfo
            if(info.errorInfo != null) return info

            fields.forEach { (key, value) ->
                run {
                    val field: Field = ReflectionUtils.findField(AccountEntity::class.java, key)!!
                    field.trySetAccessible()
                    ReflectionUtils.setField(field, account, value)
                }
            }

            accountRepository.save(account)
        }

        return info
    }
}