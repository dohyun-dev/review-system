package com.dohyundev.review.organization.department.application.port.dto

data class CreateDepartmentCommand(
    val name: String,
    val parentId: Long? = null,
)
