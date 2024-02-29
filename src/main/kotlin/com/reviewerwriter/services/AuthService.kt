package com.reviewerwriter.services

import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.response.Info
import com.reviewerwriter.dto.response.JwtInfo
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
            info.errorInfo = "Это имя пользователя уже занято"
            return info
        }

        val account = AccountEntity()
        accountRepository.save(account)

        val user = UserEntity(
            username = request.username,
            password = encoder.encode(request.password),
            accountEntity = account
        )
        userRepository.save(user)

        return info
    }

    fun logIn(request: UserDTO): JwtInfo {
        val info = JwtInfo()

        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )
        val token = jwtService.generateToken(auth)
        if(token.isNullOrEmpty()) {
            info.errorInfo = "Ошибка при создании токена"
            return info
        } else {
            info.token = token
        }

        return info;
    }
}