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
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
        val accountRepository: AccountRepository,
        val userRepository: UserRepository,
        val authenticationManager: AuthenticationManager,
        val userDetailsService: UserDetailsService,
        val jwtService: JwtService,
        val encoder: BCryptPasswordEncoder) {
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
            password = encoder.encode(request.password),
            accountEntity = account
        )
        userRepository.save(user)

        return info
    }

    fun logIn(request: UserDTO): JwtInfo {
        val info = JwtInfo()

        var auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )

        val user = userDetailsService.loadUserByUsername(request.username)
        val accessToken = createAccessToken(auth)

        if(accessToken.isNullOrEmpty()) {
            info.errorInfo = "ОШИБКА БЛЯТЬ"
        } else {
            info.token = accessToken
        }

        return info;
    }

    private fun createAccessToken(auth: Authentication) = jwtService.generateToken(auth)

}