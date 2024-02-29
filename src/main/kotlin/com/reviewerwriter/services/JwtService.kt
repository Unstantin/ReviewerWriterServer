package com.reviewerwriter.services

import com.reviewerwriter.entities.UserEntity
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}")
    val secret: String,

    @Value("\${jwt.lifetime}")
    val lifetime: Int
) {
    fun extractLogin(jwt: String): String {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(jwt)
            .body
            .subject

    }

    fun generateToken(authentication: Authentication): String? {
        val userDetails: UserEntity = authentication.principal as UserEntity
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + lifetime))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    fun isTokenValid(jwt: String?, userDetails: UserDetails): Boolean {
        val login = extractLogin(jwt!!)
        return if (login.isEmpty()) {
            false
        } else {
            login == userDetails.username
        }
    }

}