package com.reviewerwriter.entities

import jakarta.persistence.*

@Entity
@Table(name = "_access_to_review")
class AccessRoleToReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @OneToOne
    @JoinColumn(name = "review")
    var review: ReviewEntity,

    @OneToOne
    @JoinColumn(name = "access")
    var access: AccessRoleEntity,
)