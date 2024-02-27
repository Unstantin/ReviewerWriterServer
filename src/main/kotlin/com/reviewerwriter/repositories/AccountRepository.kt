package com.reviewerwriter.repositories

import com.reviewerwriter.entities.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<AccountEntity, Int> {

}