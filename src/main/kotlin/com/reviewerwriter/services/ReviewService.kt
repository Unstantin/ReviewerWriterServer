package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.requests.ReviewCreateRequest
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.entities.ReviewEntity
import com.reviewerwriter.repositories.ReviewRepository
import com.reviewerwriter.repositories.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class ReviewService(
    val reviewRepository: ReviewRepository,
    val userRepository: UserRepository
) {
    fun createReview(request: ReviewCreateRequest): Info {
        val info = Info()

        val userDetails = (SecurityContextHolder.getContext().authentication as UsernamePasswordAuthenticationToken).principal as UserDetails
        val userOptional = userRepository.findByUsername(userDetails.username)
        val account: AccountEntity
        if(!userOptional.isPresent) {
            info.errorInfo = ErrorMessages.USER_FROM_TOKEN_NOT_FOUND
            return info
        } else {
            account = userOptional.get().account

            val review = ReviewEntity(
                author = account,
                title = request.title,
                mainText = request.mainText,
                shortText = request.shortText,
                tags = request.tags
            )
            reviewRepository.save(review)
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

            info.response = reviewInfo
        } else {
            info.errorInfo = ErrorMessages.REVIEW_NOT_FOUND
        }

        return info;
    }
}