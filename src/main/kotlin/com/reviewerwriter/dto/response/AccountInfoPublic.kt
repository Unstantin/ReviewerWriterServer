package com.reviewerwriter.dto.response

import com.reviewerwriter.entities.ReviewCollectionEntity

class AccountInfoPublic(
    var id: Int,
    var nickname: String,
    var reviewCollections: List<ReviewCollectionEntity>,
    var followersN: Int,
    var followingsN: Int
)