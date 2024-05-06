package com.reviewerwriter.repositories

import com.reviewerwriter.entities.ReviewCollectionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface ReviewCollectionRepository : JpaRepository<ReviewCollectionEntity, Int> {
    @Query("SELECT rc FROM ReviewCollectionEntity rc JOIN rc.reviews r WHERE r.id = :reviewId")
    fun findByReviewId(reviewId: Int?): List<ReviewCollectionEntity>
}