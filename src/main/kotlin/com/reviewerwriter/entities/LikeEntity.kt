package com.reviewerwriter.entities

import jakarta.persistence.*

@Entity
@Table(name = "_likes")
class LikeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "account")
    var account: AccountEntity? = null,

    @ManyToOne
    @JoinColumn(name = "review")
    var review: ReviewEntity? = null
)