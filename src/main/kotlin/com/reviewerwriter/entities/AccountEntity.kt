package com.reviewerwriter.entities

import com.reviewerwriter.ListTagsConverter
import com.reviewerwriter.models.*
import jakarta.persistence.*

@Entity
@Table(name = "accounts")
class AccountEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @Column(name = "nickname", length = 15)
    var nickname: String = "",

    @Column(name = "subs_n")
    var subsN: Int = 0,

    @Column(name = "tags")
    @Convert(converter = ListTagsConverter::class)
    var tags: ListTags = ListTags()
) {
}