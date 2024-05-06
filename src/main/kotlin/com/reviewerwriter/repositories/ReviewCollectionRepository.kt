package com.reviewerwriter.repositories

import com.reviewerwriter.entities.ReviewCollectionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewCollectionRepository : JpaRepository<ReviewCollectionEntity, Int> {
}