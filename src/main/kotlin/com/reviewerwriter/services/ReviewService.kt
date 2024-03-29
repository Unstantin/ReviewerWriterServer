package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.requests.ReviewCreateRequest
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.ReviewCreateResponse
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.entities.ReviewEntity
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.ReviewRepository
import io.jsonwebtoken.Claims
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.time.LocalDate

@Service
class ReviewService(
    val reviewRepository: ReviewRepository,
    val accountRepository: AccountRepository
) {
    fun createReview(request: ReviewCreateRequest): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
            return info
        } else {
            val account = accountOptional.get()

            val review = ReviewEntity(
                author = account,
                title = request.title,
                mainText = request.mainText,
                shortText = request.shortText,
                date = LocalDate.now(),
                tags = request.tags
            )
            reviewRepository.save(review)

            info.response = ReviewCreateResponse(review.id!!)
        }

        return info
    }

    fun getReviewInfoById(id: Int) : Info {
        val info = Info()
        val reviewInfo = ReviewInfo()

        val reviewOptional = reviewRepository.findById(id)
        if(reviewOptional.isPresent) {
            val review = reviewOptional.get()
            reviewInfo.id = review.id
            reviewInfo.likesN = review.likesN
            reviewInfo.title = review.title
            reviewInfo.authorNickname = review.author.nickname
            reviewInfo.mainText = review.mainText
            reviewInfo.shortText = review.shortText
            reviewInfo.date = review.date
            reviewInfo.tags = review.tags

            info.response = reviewInfo
        } else {
            info.errorInfo = ErrorMessages.REVIEW_NOT_FOUND
        }

        return info
    }

    fun updateReviewInfo(id: Int, fields: Map<String, Any>): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)

        val reviewOptional = reviewRepository.findById(id)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else if(!reviewOptional.isPresent) {
            info.errorInfo = ErrorMessages.REVIEW_NOT_FOUND
        } else {
            val account = accountOptional.get()
            val review = reviewOptional.get()

            if(account.id != review.author.id) {
                info.errorInfo = ErrorMessages.ACCESS_IS_DENIED
                return info
            }

            fields.forEach { (key, value) ->
                run {
                    val field: Field = ReflectionUtils.findField(ReviewEntity::class.java, key)!!
                    field.trySetAccessible()
                    ReflectionUtils.setField(field, review, value)
                }
            }

            reviewRepository.save(review)
        }

        return info
    }

    fun deleteReview(id: Int): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)

        val reviewOptional = reviewRepository.findById(id)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else if(!reviewOptional.isPresent) {
            info.errorInfo = ErrorMessages.REVIEW_NOT_FOUND
        } else {
            val account = accountOptional.get()
            val review = reviewOptional.get()

            if(account.id != review.author.id) {
                info.errorInfo = ErrorMessages.ACCESS_IS_DENIED
                return info
            }

            reviewRepository.deleteById(id)
        }

        return info
    }
}