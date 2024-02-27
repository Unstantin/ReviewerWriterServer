package com.reviewerwriter.repositories

import com.reviewerwriter.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<UserEntity, Int> {
    fun findByUsername(username: String) : Optional<UserEntity>
}