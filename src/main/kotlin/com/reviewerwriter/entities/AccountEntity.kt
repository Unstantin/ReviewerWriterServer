package com.reviewerwriter.entities

import com.reviewerwriter.TagsConverter
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
    @Convert(converter = TagsConverter::class)
    var tags : ArrayList<Tag> = ArrayList()
) {
}