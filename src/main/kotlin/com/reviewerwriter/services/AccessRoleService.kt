package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.response.AccessRoleCreateResponse
import com.reviewerwriter.dto.response.AccessRoleInfo
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.entities.AccessRoleEntity
import com.reviewerwriter.repositories.AccessRoleRepository
import com.reviewerwriter.repositories.AccountRepository
import io.jsonwebtoken.Claims
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AccessRoleService(
    val accountRepository: AccountRepository,
    val accessRoleRepository: AccessRoleRepository
) {
    fun getAllCreatedAccessRoles(): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()

            val accessRoles = accessRoleRepository.findAllByOwnerId(account.id!!)
            val accessInfoList = ArrayList<AccessRoleInfo>()
            accessRoles.forEach {
                accessInfoList.add(
                    AccessRoleInfo(
                        id = it.id!!,
                        name = it.name
                    )
                )
            }

            info.response = accessInfoList
        }

        return info
    }

    fun createAccessRole(name: String): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else {
            val account = accountOptional.get()

            accessRoleRepository.findAllByOwnerId(account.id!!).forEach {
                if(it.name == name) {
                    info.errorInfo = ErrorMessages.NAME_OF_ACCESS_ROLE_IS_ALREADY_TAKEN_ON_THIS_ACCOUNT
                    return info
                }
            }

            val accessRole = accessRoleRepository.save(
                AccessRoleEntity(
                    name = name,
                    owner = account
                )
            )

            info.response = AccessRoleCreateResponse(accessRoleId = accessRole.id!!)
        }

        return info
    }

    fun deleteAccessRole(id: Int): Info {
        val info = Info()

        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        val accountOptional = accountRepository.findById(claims["accountId"] as Int)
        val accessRoleOptional = accessRoleRepository.findById(id)
        if(!accountOptional.isPresent) {
            info.errorInfo = ErrorMessages.TOKEN_ERROR
        } else if(!accessRoleOptional.isPresent) {
            info.errorInfo = ErrorMessages.ACCESS_ROLE_NOT_FOUND
        } else {
            val account = accountOptional.get()
            val accessRole = accessRoleOptional.get()

            if(account.id != accessRole.owner.id) {
                info.errorInfo = ErrorMessages.ACCESS_IS_DENIED
                return info
            }

            accessRoleRepository.delete(accessRoleRepository.findById(id).get())
        }

        return info
    }
}