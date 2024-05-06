package com.reviewerwriter.entities

import jakarta.persistence.*

@Entity
@Table(name = "_access_roles")
class AccessRoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @Column(name = "name")
    var name: String,

    @ManyToOne
    @JoinColumn(name = "owner")
    var owner: AccountEntity
)