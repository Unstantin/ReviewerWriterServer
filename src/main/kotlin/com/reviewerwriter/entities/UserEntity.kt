package com.reviewerwriter.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Int,

    @Column(name = "username")
    val username : String,

    @Column(name = "password")
    val password : String,

    @OneToOne
    val accountEntity: AccountEntity
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        //ЗАГЛУШКА ДЛЯ РОЛЕЙ ХЗ ДЕЛАТЬ ИЛИ НЕТ Я ЕБАЛ ЭТО ГАВНО )))
        return MutableList(1) { a: Int -> SimpleGrantedAuthority("USER$a") }
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}