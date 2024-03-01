package com.reviewerwriter.services

import com.reviewerwriter.dto.requests.ReviewCreateRequest
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.entities.ReviewEntity
import com.reviewerwriter.models.ListTags
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.ReviewRepository
import org.springframework.stereotype.Service

@Service
class ReviewService(
    val reviewRepository: ReviewRepository,
    val accountRepository: AccountRepository,
    val accessService: AccessService
) {
    fun createReview(request: ReviewCreateRequest): Info {
        val info = Info()
        val accessInfo = accessService.checkAccessToAccount(request.authorId).errorInfo
        if(accessInfo != null) {
            info.errorInfo = accessInfo
            return info
        }

        val listTags = ListTags()
        listTags.tags = request.tags as ArrayList
        val review = ReviewEntity(
            author = accountRepository.getReferenceById(request.authorId),
            title = request.title,
            mainText = request.mainText,
            shortText = request.shortText,
            tags = listTags
        )
        reviewRepository.save(review)

        return info
    }
}