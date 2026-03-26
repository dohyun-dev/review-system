package com.dohyundev.review.organization.department.application.port.`in`

import com.dohyundev.review.organization.department.application.port.dto.CreateDepartmentCommand
import com.dohyundev.review.organization.department.domain.entity.Department

interface CreateDepartmentUseCase {
    fun create(command: CreateDepartmentCommand): Department
}
