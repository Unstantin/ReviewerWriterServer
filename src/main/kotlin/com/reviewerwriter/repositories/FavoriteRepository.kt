package com.reviewerwriter.repositories

import com.reviewerwriter.entities.FavoriteEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FavoriteRepository : JpaRepository<FavoriteEntity, Int> {
    fun findByAccountIdAndReviewId(accountId: Int, reviewId: Int): Optional<FavoriteEntity>
    fun findAllByAccountId(accountId: Int): List<FavoriteEntity>
    fun deleteAllByReviewId(reviewId: Int)
}