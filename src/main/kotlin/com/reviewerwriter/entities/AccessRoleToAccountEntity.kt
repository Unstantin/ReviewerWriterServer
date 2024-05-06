package com.reviewerwriter.entities

import jakarta.persistence.*

@Entity
@Table(name = "_access_to_account")
class AccessRoleToAccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @OneToOne
    @JoinColumn(name = "account_id")
    var account: AccountEntity,

    @OneToOne
    @JoinColumn(name = "access")
    var access: AccessRoleEntity,
)