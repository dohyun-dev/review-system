package com.dohyundev.review.organization.member.application.port.dto

import com.dohyundev.review.organization.member.domain.entity.DepartmentMemberType

data class UpdateDepartmentMemberCommand(
    val dutyId: Long,
    val type: DepartmentMemberType,
)
