package com.reviewerwriter.entities

import jakarta.persistence.*

@Entity
class ReviewCollectionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,

    @Column(name = "owner")
    var owner: AccountEntity,

    @Column(name = "name")
    var name: String,

    @OneToMany
    @JoinColumn(name = "review_id")
    var reviews: ArrayList<ReviewEntity> = ArrayList()
)