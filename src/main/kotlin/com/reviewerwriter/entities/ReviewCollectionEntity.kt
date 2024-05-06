package com.reviewerwriter.entities

import jakarta.persistence.*

@Entity
@Table(name = "_review_collections")
class ReviewCollectionEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @Column(name = "name")
    var name: String = "",

    @ManyToMany
    @JoinTable(
        name = "_review_collections_to_reviews",
        joinColumns = [JoinColumn(name = "reviews")],
        inverseJoinColumns = [JoinColumn(name = "collections")]
    )
    var reviews: ArrayList<ReviewEntity> = ArrayList(),
)