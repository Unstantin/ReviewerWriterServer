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
    var id: Int = 0,

    @Column(name = "nickname", length = 15)
    var nickname: String = "",

    @Column(name = "subs_n")
    var subsN: Int = 0,

    @Column(name = "tags")
    @Convert(converter = AccountTagsConverter::class)
    var accountTags: AccountTags = AccountTags()
) {
    fun addTag(tagName: String, criteria: List<String>) {
        val list : ArrayList<Criteria> = ArrayList()
        for (criteriaName in criteria) {
            list.add(Criteria(criteriaName))
        }
        accountTags.tags.add(AccountTag(name = tagName, criteria = list))
    }
}