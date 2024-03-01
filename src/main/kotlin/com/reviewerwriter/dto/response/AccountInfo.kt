package com.reviewerwriter.dto.response

import com.reviewerwriter.models.ListTags

class AccountInfo(
    var nickname: String = "",
    var tags: ListTags? = null
) : Info()
