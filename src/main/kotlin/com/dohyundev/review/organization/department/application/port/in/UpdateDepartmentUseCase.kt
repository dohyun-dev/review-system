package com.dohyundev.review.organization.department.application.port.`in`

import com.dohyundev.review.organization.department.application.port.dto.UpdateDepartmentCommand
import com.dohyundev.review.organization.department.domain.entity.Department

interface UpdateDepartmentUseCase {
    fun update(id: Long, command: UpdateDepartmentCommand): Department
}
