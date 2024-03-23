package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.requests.AccountCreateTagRequest
import com.reviewerwriter.dto.response.AccountInfo
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.models.Tag
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.ReviewRepository
import io.jsonwebtoken.Claims
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field

@Service
class AccountService(
        val accountRepository: AccountRepository,
        val reviewRepository: ReviewRepository
) {

    fun getPrivateAccountInfo() : Info {
        val info = Info()
        val accountInfo = AccountInfo()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()

            accountInfo.nickname = account.nickname
            accountInfo.tags = account.tags
            info.response = accountInfo
        }

        return info
    }


    fun createAccountTag(request: AccountCreateTagRequest) : Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()

            account.tags.add(Tag(request.tagName, request.criteria))
            accountRepository.save(account)
        }

        return info
    }


    fun updateAccountInfo(fields: Map<String, Any>): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()

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

    fun getAllAccountReviews(): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()
            val listReviews = reviewRepository.findAllByAuthorId(account.id!!)
            val listReviewsInfo = ArrayList<ReviewInfo>()
            listReviews.forEach {
                listReviewsInfo.add(
                    ReviewInfo(
                        id = it.id,
                        title = it.title,
                        mainText = it.mainText,
                        shortText = it.shortText,
                        authorNickname = it.author.nickname,
                        likesN = it.likesN
                    )
                )
            }

            info.response = listReviewsInfo
        }

        return info
    }
}