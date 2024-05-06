package com.reviewerwriter.services

import com.reviewerwriter.actions.ActionToAnotherAccountReview
import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.requests.ReviewCreateRequest
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.ReviewCreateResponse
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.entities.FavoriteEntity
import com.reviewerwriter.entities.LikeEntity
import com.reviewerwriter.entities.ReviewEntity
import com.reviewerwriter.repositories.*
import io.jsonwebtoken.Claims
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.time.LocalDateTime

@Service
class ReviewService(
    val reviewRepository: ReviewRepository,
    val accountRepository: AccountRepository,
    val likeRepository: LikeRepository,
    val favoriteRepository: FavoriteRepository,
    val followRepository: FollowRepository,
    val reviewCollectionRepository: ReviewCollectionRepository
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
                date = LocalDateTime.now(),
                tags = request.tags
            )
            reviewRepository.save(review)

            info.response = ReviewCreateResponse(review.id!!)
        }

        return info
    }

    fun getReviewInfoById(id: Int) : Info {
        val info = Info()

        val reviewOptional = reviewRepository.findById(id)
        if(reviewOptional.isPresent) {
            val review = reviewOptional.get()

            val reviewInfo = ReviewInfo(
                id = review.id!!, title = review.title,
                authorNickname = review.author.nickname!!,
                mainText = review.mainText,
                shortText = review.shortText,
                date = review.date, tags = review.tags,
                likesN = likeRepository.countByReviewId(review.id!!)
            )

            info.response = reviewInfo
        } else {
            info.errorInfo = ErrorMessages.REVIEW_NOT_FOUND
        }

        return info
    }

    fun editReviewData(id: Int, fields: Map<String, Any>): Info {
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

    @Transactional
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

            reviewCollectionRepository.findByReviewId(id).stream().forEach { println(it.id) }
            favoriteRepository.deleteAllByReviewId(id)
            likeRepository.deleteAllByReviewId(id)
            reviewRepository.deleteById(id)

            //TODO УДАЛЯТЬ ИЗ КОЛЛЕКЦИЙ
        }

        return info
    }

    fun actionToReview(id: Int, action: ActionToAnotherAccountReview) : Info {
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

            when(action) {
                ActionToAnotherAccountReview.TO_LIKE -> {
                    likeRepository.save(LikeEntity( account = account, review = review ))
                }
                ActionToAnotherAccountReview.TO_UNLIKE -> {
                    val like = likeRepository.findByAccountIdAndReviewId(account.id!!, review.id!!)
                    likeRepository.delete(like.get())
                }
                ActionToAnotherAccountReview.TO_FAVORITE -> {
                    favoriteRepository.save(FavoriteEntity( account = account, review = review ))
                }
                ActionToAnotherAccountReview.TO_REMOVE_FAVORITE -> {
                    val favorite = favoriteRepository.findByAccountIdAndReviewId(account.id!!, review.id!!)
                    favoriteRepository.delete(favorite.get())
                }
            }
        }

        return info
    }

    fun getFollowingReviews(page: Int): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)

        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()

            val followingAccounts = followRepository.findAllByFollowerId(account.id!!).stream().map { it.following }.toList()
            val accountsReviews: ArrayList<ReviewEntity> = ArrayList()
            followingAccounts.forEach {
                accountsReviews.addAll(
                    reviewRepository.findAllByAuthorIdAndDateGreaterThan(
                        it?.id!!, pageable=PageRequest.of(page, 10)
                    )
                )
            }
            val sortedReviews = accountsReviews.stream().sorted(compareBy<ReviewEntity?> { it?.date }.reversed()).toList()
            val accountsReviewsInfo = ArrayList<ReviewInfo>()
            sortedReviews.forEach {
                accountsReviewsInfo.add(
                    ReviewInfo(
                        id = it.id!!, title = it.title,
                        mainText = it.mainText, shortText = it.shortText,
                        authorNickname = it.author.nickname!!,
                        date = it.date, tags = it.tags,
                        likesN = likeRepository.countByReviewId(it.id!!)
                    )
                )
            }

            info.response = accountsReviewsInfo
        }

        return info
    }
}