package com.reviewerwriter.dto.response

import com.reviewerwriter.models.Tag
import java.time.LocalDateTime

class ReviewInfo(
    var id: Int,
    var title: String,
    var mainText: String,
    var shortText: String,
    var authorNickname: String,
    var date: LocalDateTime,
    var tags: ArrayList<Tag>,
    var likesN: Int
)