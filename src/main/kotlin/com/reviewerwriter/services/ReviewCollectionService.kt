package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.requests.AddOrRemoveReviewToCollectionRequest
import com.reviewerwriter.dto.requests.ReviewCollectionCreateRequest
import com.reviewerwriter.dto.requests.ReviewCollectionCreateResponse
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.entities.ReviewCollectionEntity
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.ReviewCollectionRepository
import com.reviewerwriter.repositories.ReviewRepository
import io.jsonwebtoken.Claims
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field

@Service
class ReviewCollectionService(
    val accountRepository: AccountRepository,
    val reviewCollectionRepository: ReviewCollectionRepository,
    val reviewRepository: ReviewRepository
) {
    fun createReviewCollection(request: ReviewCollectionCreateRequest): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()
            info.response = ReviewCollectionCreateResponse(reviewCollectionId =
                reviewCollectionRepository.save(
                    ReviewCollectionEntity(
                        owner = account,
                        name = request.name
                    )
                ).id!!
            )
        }

        return info
    }

    fun addOrRemoveReviewToCollection(id: Int, request: AddOrRemoveReviewToCollectionRequest): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val reviewOptional = reviewRepository.findById(request.reviewId)
        val reviewCollectionOptional = reviewCollectionRepository.findById(id)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else if(!reviewOptional.isPresent) {
            info.errorInfo = ErrorMessages.REVIEW_NOT_FOUND
        } else if(!reviewCollectionOptional.isPresent) {
            info.errorInfo = ErrorMessages.REVIEW_COLLECTION_NOT_FOUND
        } else {
            val account = accountOptional.get()
            val review = reviewOptional.get()
            val collection = reviewCollectionOptional.get()

            if(review.author.id != account.id || collection.owner.id != account.id) {
                info.errorInfo = ErrorMessages.ACCESS_IS_DENIED
                return info
            }

            if(request.add) { collection.reviews.add(review)
            } else { collection.reviews.remove(review) }

            reviewCollectionRepository.save(collection)
        }

        return info
    }

    fun deleteReviewCollection(id: Int): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val reviewCollectionOptional = reviewCollectionRepository.findById(id)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else if (!reviewCollectionOptional.isPresent) {
            info.errorInfo = ErrorMessages.REVIEW_COLLECTION_NOT_FOUND
        } else {
            val account = accountOptional.get()
            val collection = reviewCollectionOptional.get()

            if(account.id != collection.owner.id) {
                info.errorInfo = ErrorMessages.ACCESS_IS_DENIED
                return info
            }

            reviewCollectionRepository.deleteById(id)
        }

        return info
    }

    fun updateReviewCollectionInfo(collectionId: Int, fields: Map<String, Any>) : Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val reviewCollectionOptional = reviewCollectionRepository.findById(collectionId)
        if (!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else if(!reviewCollectionOptional.isPresent) {
            info.errorInfo = ErrorMessages.REVIEW_COLLECTION_NOT_FOUND
        } else {
            val account = accountOptional.get()
            val collection = reviewCollectionOptional.get()

            if(account.id != collection.owner.id) {
                info.errorInfo = ErrorMessages.ACCESS_IS_DENIED
                return info
            }

            fields.forEach { (key, value) ->
                run {
                    val field: Field = ReflectionUtils.findField(ReviewCollectionEntity::class.java, key)!!
                    field.trySetAccessible()
                    ReflectionUtils.setField(field, collection, value)
                }
            }

            reviewCollectionRepository.save(collection)
        }

        return info
    }
}