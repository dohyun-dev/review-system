package com.dohyundev.review.review.command.domain.group.exception

class ReviewGroupNotFoundException(message: String = "리뷰 그룹을 찾을 수 없습니다.") : NoSuchElementException(message)
