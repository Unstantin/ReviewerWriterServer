package com.reviewerwriter.entities

import com.reviewerwriter.AccountTagsConverter
import com.reviewerwriter.models.*
import jakarta.persistence.*

@Entity
@Table(name = "accounts")
class AccountEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Int,

    @Column(name = "nickname", length = 15)
    val nickname: String,

    @Column(name = "subs_n")
    val subsN: Int,

    @Column(name = "tags")
    @Convert(converter = AccountTagsConverter::class)
    val accountTags: AccountTags
) {
    fun addTag(tagName: String, criteria: List<String>) {
        val list : ArrayList<Criteria> = ArrayList()
        for (criteriaName in criteria) {
            list.add(Criteria(criteriaName))
        }
        accountTags.tags.add(AccountTag(name = tagName, criteria = list))
    }
}