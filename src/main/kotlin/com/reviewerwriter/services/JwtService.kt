package com.reviewerwriter.services

import com.reviewerwriter.entities.UserEntity
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
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
    fun extractUsername(jwt: String): String {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(jwt)
            .body["username"] as String
    }

    fun getUsernameFromToken() : String {
        val claims = SecurityContextHolder.getContext().authentication.credentials as Claims
        return claims["username"] as String
    }

    fun generateToken(authentication: Authentication): String? {
        val user: UserEntity = authentication.principal as UserEntity
        val claims = mapOf(
            "username" to user.username,
            "userId" to user.id,
            "accountId" to user.account.id
        )

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + lifetime))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    fun isTokenValid(jwt: String?, userDetails: UserDetails): Boolean {
        val login = extractUsername(jwt!!)
        return if (login.isEmpty()) {
            false
        } else {
            login == userDetails.username
        }
    }

    fun getAllClaimsFromToken(token: String): Claims? {
        val claims: Claims? = try {
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }
        return claims
    }
}