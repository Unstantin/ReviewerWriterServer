package com.reviewerwriter.configs

import com.reviewerwriter.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfig(
    val userRepository: UserRepository
) {
    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService? {
        return UserDetailsService { username: String -> run {
                val user = this@ApplicationConfig.userRepository.findByUsername(username)
                if(this@ApplicationConfig.userRepository.findByUsername(username).isPresent) {
                    user.get()
                } else {
                    null
                }
            }
        }
    }
    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()
    @Bean
    fun authenticationProvider(userRepository: UserRepository): AuthenticationProvider =
            DaoAuthenticationProvider()
                    .also {
                        it.setUserDetailsService(userDetailsService(userRepository))
                        it.setPasswordEncoder(encoder())
                    }
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
            config.authenticationManager
}