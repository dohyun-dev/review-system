package com.dohyundev.review.review.command.domain.group.exception

class InsufficientReviewCountException : IllegalStateException("리뷰를 시작하려면 리뷰가 한 개 이상 있어야 합니다.")
