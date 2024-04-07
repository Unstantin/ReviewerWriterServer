package com.reviewerwriter.repositories

import com.reviewerwriter.entities.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<AccountEntity, Int> {
    fun findByNickname(nickname: String) : Optional<AccountEntity>
}