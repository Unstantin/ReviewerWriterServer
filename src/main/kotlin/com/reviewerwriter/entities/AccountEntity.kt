package com.reviewerwriter.entities

import com.google.gson.annotations.SerializedName
import com.reviewerwriter.TagsConverter
import com.reviewerwriter.models.*
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "_accounts")
class AccountEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @Column(name = "nickname")
    var nickname: String? = null,

    @Convert(converter = TagsConverter::class)
    @Column(columnDefinition = "jsonb")
    @SerializedName("tags")
    @JdbcTypeCode(SqlTypes.JSON)
    var tags: ArrayList<Tag> = ArrayList()
)