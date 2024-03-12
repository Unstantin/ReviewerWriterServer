package com.reviewerwriter.dto.response

import com.reviewerwriter.ErrorMessages

open class Info (
    var errorInfo: ErrorMessages? = null,
    var response: Any? = null
)