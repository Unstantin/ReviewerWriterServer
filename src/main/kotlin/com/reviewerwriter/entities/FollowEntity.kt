package com.reviewerwriter.entities

import jakarta.persistence.*

@Entity
@Table(name = "followings")
class FollowEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "follower")
    var follower: AccountEntity? = null,

    @ManyToOne
    @JoinColumn(name = "following")
    var following: AccountEntity? = null
) {

}