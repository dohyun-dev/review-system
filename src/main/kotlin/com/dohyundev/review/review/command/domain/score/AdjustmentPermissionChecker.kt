package com.dohyundev.review.review.command.domain.score

interface AdjustmentPermissionChecker {
    fun check(employeeId: Long, reviewGroupId: Long)
}
