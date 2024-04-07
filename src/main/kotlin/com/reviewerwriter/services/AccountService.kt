package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.response.AccountInfoPrivate
import com.reviewerwriter.dto.response.AccountInfoPublic
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.entities.FollowEntity
import com.reviewerwriter.repositories.*
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
        val followRepository: FollowRepository,
        val favoriteRepository: FavoriteRepository,
        val likeRepository: LikeRepository
) {

    fun getAccountInfoPrivate() : Info {
        val info = Info()
        val accountInfo = AccountInfoPrivate()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()

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
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()

            fields.forEach { (key, value) ->
                if(key == "nickname" && accountRepository.findByNickname(value.toString()).isEmpty) {
                    run {
                        val field: Field = ReflectionUtils.findField(AccountEntity::class.java, key)!!
                        field.trySetAccessible()
                        ReflectionUtils.setField(field, account, value)
                    }
                } else {
                    info.errorInfo = ErrorMessages.NICKNAME_IS_ALREADY_TAKEN
                    return info
                }
            }

            accountRepository.save(account)
        }

        return info
    }

    fun getAllAccountReviews(accountId: Int? = null, isMyAccount: Boolean = true): Info {
        val info = Info()

        val accountOptional: Optional<AccountEntity>
        if(isMyAccount) { //если запрос на свой аккаунт, то пытаемся достать из токена
            val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
            accountOptional = accountRepository.findById(claims["accountId"] as Int)
        } else {
            accountOptional = accountRepository.findById(accountId!!)
        }

        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()
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
                        date = it.date,
                        tags = it.tags,
                        likesN = likeRepository.findByReviewId(it.id!!).stream().count().toInt()
                    )
                )
            }

            info.response = listReviewsInfo
        }

        return info
    }

    fun getAllFollow(isFollowers : Boolean, isMyAccount: Boolean = true, accountId: Int? = null): Info {
        val info = Info()

        val accountOptional: Optional<AccountEntity>
        if(isMyAccount) { //если запрос на свой аккаунт, то пытаемся достать из токена
            val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
            accountOptional = accountRepository.findById(claims["accountId"] as Int)
        } else {
            accountOptional = accountRepository.findById(accountId!!)
        }

        if(!accountOptional.isPresent) {
            if(isMyAccount) { //ошибка токена выбрасыватся только если запрос о своем аккаунте
                info.errorInfo = ErrorMessages.TOKEN_ERROR
            } else {
                info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
            }
        } else {
            val account = accountOptional.get()

            val follow: List<AccountEntity?> = if(isFollowers) {
                followRepository.findAllByFollowingId(account.id!!).stream().map { it.follower }.toList()
            } else {
                followRepository.findAllByFollowerId(account.id!!).stream().map { it.following }.toList()
            }

            val followAccountInfo = ArrayList<AccountInfoPublic>()
            follow.forEach {
                if (it != null) {
                    followAccountInfo.add(
                        AccountInfoPublic(
                            id = it.id,
                            nickname = it.nickname
                        )
                    )
                }
            }

            info.response = followAccountInfo
        }

        return info
    }

    fun getAccountInfoPublic(id: Int): Info {
        val info = Info()

        val accountOptional = accountRepository.findById(id)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            val account = accountOptional.get()

            info.response = AccountInfoPublic(
                id = account.id,
                nickname = account.nickname
            )
        }

        return info;
    }

    fun followToAccount(id: Int): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val myAccountOptional = accountRepository.findById(claims["accountId"] as Int)
        val myAccount: AccountEntity
        if(!myAccountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
            return info
        } else {
            myAccount = myAccountOptional.get()
            if(myAccount.id == id) {
                info.errorInfo = ErrorMessages.ATTEMPT_FOLLOW_YOURSELF
                return info
            }
        }

        val accountOptional = accountRepository.findById(id)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCOUNT_NOT_FOUND
        } else {
            val account = accountOptional.get()

            val follow = followRepository.findByFollowerIdAndFollowingId(myAccount.id!!, account.id!!)
            if(follow.isPresent) {
                followRepository.delete(follow.get())
            } else {
                val newFollow = FollowEntity(
                    follower = myAccount,
                    following = account
                )
                followRepository.save(newFollow)
            }
        }

        return info;
    }

    fun getAllFavoriteReviews(): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()

            val listReviews = favoriteRepository.findAllByAccountId(account.id!!).stream().map { it.review }.toList()
            val listReviewsInfo = ArrayList<ReviewInfo>()
            listReviews.forEach {
                if (it != null) {
                    listReviewsInfo.add(
                        ReviewInfo(
                            id = it.id,
                            title = it.title,
                            mainText = it.mainText,
                            shortText = it.shortText,
                            authorNickname = it.author.nickname,
                            date = it.date,
                            tags = it.tags,
                            likesN = likeRepository.findByReviewId(it.id!!).stream().count().toInt()
                        )
                    )
                }
            }

            info.response = listReviewsInfo
        }

        return info
    }
}