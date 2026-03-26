package com.dohyundev.review.organization.member.application.port.`in`

import com.dohyundev.review.organization.member.application.port.dto.AppendDepartmentMemberCommand
import com.dohyundev.review.organization.member.domain.entity.DepartmentMember

interface AppendDepartmentMemberUseCase {
    fun append(command: AppendDepartmentMemberCommand): DepartmentMember
}
