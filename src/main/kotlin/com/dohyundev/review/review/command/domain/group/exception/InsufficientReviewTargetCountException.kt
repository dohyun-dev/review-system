package com.dohyundev.review.review.command.domain.group.exception

class InsufficientReviewTargetCountException : IllegalStateException("리뷰를 시작하려면 리뷰 대상자가 한 명 이상 있어야 합니다.")
