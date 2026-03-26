package com.dohyundev.review.organization.department.application.port.dto

data class UpdateDepartmentCommand(
    val name: String,
    val parentId: Long? = null,
)
