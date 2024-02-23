package com.reviewerwriter.services

import com.reviewerwriter.dto.requests.UserCreateTagRequest
import com.reviewerwriter.dto.requests.UserPutRequest
import com.reviewerwriter.dto.response.UserInfo
import com.reviewerwriter.entities.UserEntity
import com.reviewerwriter.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun getUserInfo(userId: Int) : UserInfo {
        val user: UserEntity = userRepository.getReferenceById(userId)
        var tags = ""
        user.userTags.tags.forEach {
            tags += it.toString()
        }
        return UserInfo(username = user.username, tags=tags)
    }

    fun putUser(userPutRequest: UserPutRequest) {
        userRepository.save(UserEntity(username = userPutRequest.username))
    }

    fun createTag(userCreateTagRequest: UserCreateTagRequest) {
        val user: UserEntity = userRepository.getReferenceById(userCreateTagRequest.userId)
        user.addTag(userCreateTagRequest.tagName, userCreateTagRequest.criteria.split(","))
        userRepository.save(user)
    }
}