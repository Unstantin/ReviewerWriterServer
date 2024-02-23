package com.reviewerwriter.entities

import com.reviewerwriter.Role
import com.reviewerwriter.UserTagListConverter
import com.reviewerwriter.models.Criteria
import com.reviewerwriter.models.UserTag
import com.reviewerwriter.models.UserTags
import jakarta.persistence.*

@Entity
@Table(name = "Users")
data class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Int = 0,

    @Column(name = "username", length = 15)
    val username: String,

    @Column(name = "subsN")
    val subsN: Int = 0,

    @Column(name = "tags")
    @Convert(converter = UserTagListConverter::class)
    var userTags: UserTags = UserTags(),


) {
    fun addTag(tagName: String, criteria: List<String>) {
        val list : ArrayList<Criteria> = ArrayList()
        for (criteriaName in criteria) {
            list.add(Criteria(criteriaName))
        }
        userTags.tags.add(UserTag(name = tagName, criteria = list))
    }
}