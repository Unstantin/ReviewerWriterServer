package com.reviewerwriter.repositories

import com.reviewerwriter.entities.ReviewEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ReviewRepository : JpaRepository<ReviewEntity, Int> {
    fun findAllByAuthorId(id: Int) : List<ReviewEntity>
    fun findAllByAuthorIdAndDateGreaterThan(
        id: Int,
        date: LocalDateTime = LocalDateTime.now().minusMinutes(2),
        pageable: PageRequest
    ): List<ReviewEntity>
}