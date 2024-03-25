package com.reviewerwriter.repositories

import com.reviewerwriter.entities.LogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<LogEntity, Int> {
}