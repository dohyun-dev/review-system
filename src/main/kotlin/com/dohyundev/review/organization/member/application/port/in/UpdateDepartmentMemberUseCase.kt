package com.dohyundev.review.organization.member.application.port.`in`

import com.dohyundev.review.organization.member.application.port.dto.UpdateDepartmentMemberCommand
import com.dohyundev.review.organization.member.domain.entity.DepartmentMember

interface UpdateDepartmentMemberUseCase {
    fun update(id: Long, command: UpdateDepartmentMemberCommand): DepartmentMember
}
