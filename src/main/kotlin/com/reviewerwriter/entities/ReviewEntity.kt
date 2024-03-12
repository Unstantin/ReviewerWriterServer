package com.reviewerwriter.entities

import com.google.gson.annotations.SerializedName
import com.reviewerwriter.TagsConverter
import com.reviewerwriter.models.Tag
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "reviews")
class ReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: AccountEntity,

    @Column(name = "title")
    var title: String,

    @Column(name = "likes_n")
    var likesN: Int = 0,

    @Column(name = "main_text")
    var mainText: String,

    @Column(name = "short_text")
    var shortText: String = "",

    @Convert(converter = TagsConverter::class)
    @Column(columnDefinition = "jsonb")
    @SerializedName("tags")
    @JdbcTypeCode(SqlTypes.JSON)
    var tags: ArrayList<Tag> = ArrayList()
)