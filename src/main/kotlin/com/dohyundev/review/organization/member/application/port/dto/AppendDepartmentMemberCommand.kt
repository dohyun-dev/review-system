package com.dohyundev.review.organization.member.application.port.dto

import com.dohyundev.review.organization.member.domain.entity.DepartmentMemberType

data class AppendDepartmentMemberCommand(
    val departmentId: Long,
    val employeeId: Long,
    val dutyId: Long,
    val type: DepartmentMemberType,
)
