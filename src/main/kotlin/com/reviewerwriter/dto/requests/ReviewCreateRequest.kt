package com.reviewerwriter.dto.requests

import com.reviewerwriter.models.Tag

data class ReviewCreateRequest(
    val authorId: Int,
    val title: String,
    val mainText: String,
    val shortText: String,
    val tags: List<Tag>
) {
}