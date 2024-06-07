package com.reviewerwriter.dto.response

class ReviewInfoShort(
    var id: Int? = null,
    var title: String? = null,
    var shortText: String? = null,
    var authorNickname: String? = null,
    var photos: ArrayList<String>? = null
)