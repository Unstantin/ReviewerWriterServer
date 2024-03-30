package com.reviewerwriter.repositories

import com.reviewerwriter.entities.FollowEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FollowRepository : JpaRepository<FollowEntity, Int> {
    fun findAllByFollowingId(id: Int) : List<FollowEntity>
    fun findAllByFollowerId(id: Int): List<FollowEntity>

    fun findByFollowerIdAndFollowingId(followerId: Int, followingId: Int): Optional<FollowEntity>

}