package com.reviewerwriter.dto.requests

import com.reviewerwriter.models.Tag

data class ReviewCreateRequest(
    val title: String,
    val mainText: String,
    val shortText: String,
    val tags: ArrayList<Tag>
) {
}