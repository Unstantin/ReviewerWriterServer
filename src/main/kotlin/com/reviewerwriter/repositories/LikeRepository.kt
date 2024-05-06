package com.reviewerwriter.repositories

import com.reviewerwriter.entities.LikeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface LikeRepository : JpaRepository<LikeEntity, Int> {
    fun findByAccountIdAndReviewId(accountId: Int, reviewId: Int) : Optional<LikeEntity>
    fun findByReviewId(reviewId: Int) : Optional<LikeEntity>
    fun countByReviewId(accountId: Int): Int
    fun deleteAllByReviewId(id: Int)
}