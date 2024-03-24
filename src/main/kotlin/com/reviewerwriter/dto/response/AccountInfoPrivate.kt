package com.reviewerwriter.dto.response

import com.reviewerwriter.models.Tag

class AccountInfoPrivate(
    var nickname: String? = null,
    var tags: ArrayList<Tag>? = null
)
