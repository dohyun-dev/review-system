package com.dohyundev.review.review.template.domain.exception

import java.util.NoSuchElementException

class ReviewTemplateNotFoundException(
    message: String = "리뷰 템플릿이 존재하지 않습니다.",
) : NoSuchElementException(message)
