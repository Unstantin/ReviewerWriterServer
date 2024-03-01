package com.reviewerwriter.repositories

import com.reviewerwriter.entities.ReviewEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<ReviewEntity, Int> {

}