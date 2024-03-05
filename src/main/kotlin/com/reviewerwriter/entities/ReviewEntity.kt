package com.reviewerwriter.entities

import com.reviewerwriter.TagsConverter
import com.reviewerwriter.models.Tag
import jakarta.persistence.*

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

    @Column(name = "tags")
    @Convert(converter = TagsConverter::class)
    var tags: ArrayList<Tag> = ArrayList()
)