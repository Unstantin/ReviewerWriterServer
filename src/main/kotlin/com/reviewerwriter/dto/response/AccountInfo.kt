package com.reviewerwriter.dto.response

import com.reviewerwriter.models.Tag

class AccountInfo(
    var nickname: String = "",
    var tags: ArrayList<Tag>? = null
) : Info()
