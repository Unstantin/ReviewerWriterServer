package com.reviewerwriter.configs

import com.reviewerwriter.entities.UserEntity
import com.reviewerwriter.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
class ApplicationConfig(
    val userRepository: UserRepository
) {
    @Bean
    fun userDetailsService(): UserDetailsService? {
        return UserDetailsService { username: String -> run {
                val user = userRepository.findByUsername(username)
                if(userRepository.findByUsername(username).isPresent) {
                    user.get()
                } else {
                    null
                }
            }
        }
    }
}