package com.reviewerwriter.configs

import com.reviewerwriter.jwt.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    var jwtFilter: JwtFilter
) {
    @Bean
    fun filterChain(http : HttpSecurity) : SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
        http.cors { cors -> cors.disable() }
        http.formLogin { form -> form.disable() }
        http.httpBasic { basic -> basic.disable() }
        http.sessionManagement {
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.authorizeHttpRequests {
            req -> (
                    req
                        .requestMatchers("/auth").permitAll()
                        .requestMatchers("/account").authenticated()
                        .anyRequest().permitAll()
                    )
        }
        return http.build()
    }
}