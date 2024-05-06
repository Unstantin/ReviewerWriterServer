package com.reviewerwriter.repositories

import com.reviewerwriter.entities.AccessRoleToReviewEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccessRoleToReviewRepository : JpaRepository<AccessRoleToReviewEntity, Int> {
}