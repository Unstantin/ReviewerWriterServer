package com.reviewerwriter.services

import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.entities.UserEntity
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.UserRepository
import org.apache.catalina.User
import org.springframework.stereotype.Service

@Service
class AuthService(val accountRepository: AccountRepository, val userRepository: UserRepository) {
    fun registration(request: UserDTO): Info {
        val info = Info()

        if(!userRepository.findByUsername(request.username).isEmpty) {
            info.errorInfo = "User with this username is already exist"
            return info
        }

        val account = AccountEntity()
        accountRepository.save(account)

        val user = UserEntity(
            username = request.username,
            password = request.password,
            accountEntity = account
        )
        userRepository.save(user)

        return info
    }

    fun logIn(request: UserDTO): Info {
        val info = Info()
        //TODO
        return info;
    }

}