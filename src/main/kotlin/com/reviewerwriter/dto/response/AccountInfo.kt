package com.reviewerwriter.dto.response

import com.reviewerwriter.models.AccountTags

class AccountInfo(
    var nickname: String = "",
    var tags: AccountTags? = null
) : Info()
