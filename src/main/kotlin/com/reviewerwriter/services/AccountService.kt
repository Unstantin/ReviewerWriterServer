package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.response.AccountInfoPrivate
import com.reviewerwriter.dto.response.AccountInfoPublic
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.entities.FollowEntity
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.FollowRepository
import com.reviewerwriter.repositories.ReviewRepository
import io.jsonwebtoken.Claims
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList

@Service
class AccountService(
        val accountRepository: AccountRepository,
        val reviewRepository: ReviewRepository,
        val followRepository: FollowRepository
) {

    fun getAccountInfoPrivate() : Info {
        val info = Info()
        val accountInfo = AccountInfoPrivate()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            account = accountOptional.get()

            accountInfo.nickname = account.nickname
            accountInfo.tags = account.tags
            info.response = accountInfo
        }

        return info
    }

    fun updateAccountInfo(fields: Map<String, Any>): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
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
            info.errorInfo = ErrorMessages.TOKEN_ERROR
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

    fun getAllFollow(isFollowers : Boolean, isMyAccount: Boolean = true, id: Int? = null): Info {
        val info = Info()

        val accountOptional: Optional<AccountEntity>
        val account: AccountEntity
        if(isMyAccount) { //если запрос на свой аккаунт, то пытаемся достать из токена
            val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
            accountOptional = accountRepository.findById(claims["accountId"] as Int)
        } else {
            accountOptional = accountRepository.findById(id!!)
        }

        if(!accountOptional.isPresent) {
            if(isMyAccount) { //ошибка токена выбрасыватся только если запрос о своем аккаунте
                info.errorInfo = ErrorMessages.TOKEN_ERROR
            } else {
                info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
            }
        } else {
            account = accountOptional.get()

            val follow: List<AccountEntity?> = if(isFollowers) {
                followRepository.findAllByFollowingId(account.id!!).stream().map { it.follower }.toList()
            } else {
                followRepository.findAllByFollowerId(account.id!!).stream().map { it.following }.toList()
            }

            val followAccountInfo = ArrayList<AccountInfoPublic>()
            follow.forEach {
                followAccountInfo.add(
                    AccountInfoPublic(
                        nickname = it!!.nickname
                    )
                )
            }

            info.response = followAccountInfo
        }

        return info
    }

    fun getAccountInfoPublic(id: Int): Info {
        val info = Info()

        val accountOptional = accountRepository.findById(id)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()

            info.response = AccountInfoPublic(
                nickname = account.nickname
            )
        }

        return info;
    }

    fun followToAccount(id: Int, mode: Boolean): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val myAccountOptional = accountRepository.findById(claims["accountId"] as Int)
        val myAccount: AccountEntity
        if(!myAccountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
            return info
        } else {
            myAccount = myAccountOptional.get()
        }

        val accountOptional = accountRepository.findById(id)
        val account: AccountEntity
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            account = accountOptional.get()

            val follow = FollowEntity(
                follower = myAccount,
                following = account
            )

            if(mode) {
                followRepository.save(follow)
            } else {
                followRepository.delete(followRepository.findByFollowerIdAndFollowingId(myAccount.id!!, account.id!!))
            }
        }

        return info;
    }
}