package com.reviewerwriter.services

import com.reviewerwriter.ErrorMessages
import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.JwtInfo
import com.reviewerwriter.dto.response.RegistrationInfo
import com.reviewerwriter.entities.UserEntity
import com.reviewerwriter.entities.AccountEntity
import com.reviewerwriter.repositories.AccountRepository
import com.reviewerwriter.repositories.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
        val accountRepository: AccountRepository,
        val userRepository: UserRepository,
        val authenticationManager: AuthenticationManager,
        val jwtService: JwtService,
        val encoder: BCryptPasswordEncoder) {
    fun registration(request: UserDTO): Info {
        val info = Info()

        if(!userRepository.findByUsername(request.username).isEmpty) {
            info.errorInfo = ErrorMessages.USERNAME_IS_ALREADY_TAKEN
            return info
        }

        var account = AccountEntity()
        account = accountRepository.save(
            accountRepository.save(account).apply { nickname = "id" + account.id }
        )

        val user = UserEntity(
            username = request.username,
            password = encoder.encode(request.password),
            account = account
        )
        userRepository.save(user)

        info.response = RegistrationInfo(user.account.id)

        return info
    }

    fun logIn(request: UserDTO): Info {
        val info = Info()
        val jwtInfo = JwtInfo()

        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )
        val token = jwtService.generateToken(auth)
        if(token.isNullOrEmpty()) {
            info.errorInfo = ErrorMessages.ERROR_CREATING_TOKEN
            return info
        } else {
            jwtInfo.token = token
            info.response = jwtInfo
        }

        return info
    }
}