package com.dohyundev.review.review.command.domain.group.exception

class InsufficientReviewerCountException : IllegalStateException("리뷰를 시작하려면 리뷰어가 한 명 이상 있어야 합니다.")
