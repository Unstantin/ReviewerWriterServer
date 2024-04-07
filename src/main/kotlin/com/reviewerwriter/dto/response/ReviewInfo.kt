package com.reviewerwriter.dto.response

import com.reviewerwriter.models.Tag
import java.time.LocalDateTime

class ReviewInfo(
    var id: Int? = null,
    var title: String? = null,
    var mainText: String? = null,
    var shortText: String? = null,
    var authorNickname: String? = null,
    var date: LocalDateTime? = null,
    var tags: ArrayList<Tag>? = null,
    var likesN: Int? = null
)