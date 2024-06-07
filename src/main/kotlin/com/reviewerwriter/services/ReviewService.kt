package com.reviewerwriter.services

import com.reviewerwriter.ActionToAnotherAccountReview
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
import org.springframework.util.ReflectionUtils
import java.io.File
import java.lang.reflect.Field
import java.time.LocalDateTime

@Service
class ReviewService(
    val reviewRepository: ReviewRepository,
    val accountRepository: AccountRepository,
    val likeRepository: LikeRepository,
    val favoriteRepository: FavoriteRepository,
    val followRepository: FollowRepository
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
                tags = request.tags,
                photos = request.photos
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
            reviewInfo.title = review.title
            reviewInfo.authorNickname = review.author.nickname
            reviewInfo.mainText = review.mainText
            reviewInfo.shortText = review.shortText
            reviewInfo.date = review.date
            reviewInfo.tags = review.tags
            reviewInfo.likesN = likeRepository.findByReviewId(review.id!!).stream().count().toInt()
            reviewInfo.photos = review.photos

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

            review.photos.forEach { File(it).delete() }

            reviewRepository.deleteById(id)
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
                    val like = likeRepository.findByAccountIdAndReviewId(account.id!!, review.id!!)
                    if(like.isPresent) { likeRepository.delete(like.get()) }
                    else { likeRepository.save(LikeEntity( account = account, review = review )) }
                }
                ActionToAnotherAccountReview.TO_FAVORITE -> {
                    val favorite = favoriteRepository.findByAccountIdAndReviewId(account.id!!, review.id!!)
                    if(favorite.isPresent) { favoriteRepository.delete(favorite.get()) }
                    else { favoriteRepository.save(FavoriteEntity( account = account, review = review )) }
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
                    ReviewInfo().apply {
                        id = it.id
                        title = it.title
                        mainText = it.mainText
                        shortText = it.shortText
                        authorNickname = it.author.nickname
                        date = it.date
                        tags = it.tags
                        likesN = likeRepository.findByReviewId(it.id!!).stream().count().toInt()
                    }
                )
            }

            info.response = accountsReviewsInfo
        }

        return info
    }

    fun getAllReviews(): Info {
        val info = Info()
        val reviews = reviewRepository.findAll()
        val reviewsInfoArray = ArrayList<ReviewInfo>()
        reviews.forEach {
            reviewsInfoArray.add(
                ReviewInfo(
                    id = it.id,
                    title = it.title,
                    shortText = it.shortText,
                    authorNickname = it.author.nickname,
                    photos = it.photos
                )
            )
        }

        info.response = reviewsInfoArray
        return info
    }
}