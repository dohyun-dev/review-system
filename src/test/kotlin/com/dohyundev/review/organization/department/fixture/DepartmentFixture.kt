package com.dohyundev.review.organization.department.fixture

import com.dohyundev.review.organization.department.domain.entity.Department

object DepartmentFixture {
    fun create(
        name: String = "개발팀",
        parent: Department? = null,
    ): Department {
        return Department(
            name = name,
            parent = parent,
        )
    }
}
