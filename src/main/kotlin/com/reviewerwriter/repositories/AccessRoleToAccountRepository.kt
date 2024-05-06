package com.reviewerwriter.repositories

import com.reviewerwriter.entities.AccessRoleToAccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface AccessRoleToAccountRepository : JpaRepository<AccessRoleToAccountEntity, Int> {
    fun findByAccountIdAndAccessId(accountId: Int, accessId: Int): Optional<AccessRoleToAccountEntity>
}