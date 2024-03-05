package com.reviewerwriter.dto.requests

import com.reviewerwriter.models.Criteria

data class AccountCreateTagRequest(
    val tagName: String,
    val criteria: ArrayList<Criteria>
)
