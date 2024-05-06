package com.reviewerwriter.repositories

import com.reviewerwriter.entities.AccessRoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccessRoleRepository : JpaRepository<AccessRoleEntity, Int> {
    fun findAllByOwnerId(id: Int) : List<AccessRoleEntity>
}