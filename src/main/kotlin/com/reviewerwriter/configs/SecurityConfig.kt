package com.reviewerwriter.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig() {
    @Bean
    fun filterChain(http : HttpSecurity) : SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
        http.cors { cors -> cors.disable() }
        http.formLogin { form -> form.disable() }
        http.httpBasic { basic -> basic.disable() }
        http.authorizeHttpRequests {
            req -> (
                    req.anyRequest().permitAll()
                    )
        }
        http.sessionManagement {
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        return http.build()
    }
}