package com.reviewerwriter.entities

import com.google.gson.annotations.SerializedName
import com.reviewerwriter.TagsConverter
import com.reviewerwriter.models.Tag
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

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

    @Column(name = "main_text")
    var mainText: String,

    @Column(name = "short_text")
    var shortText: String = "",

    @Column(name = "date")
    var date: LocalDateTime,

    @Convert(converter = TagsConverter::class)
    @Column(columnDefinition = "jsonb")
    @SerializedName("tags")
    @JdbcTypeCode(SqlTypes.JSON)
    var tags: ArrayList<Tag> = ArrayList()
)