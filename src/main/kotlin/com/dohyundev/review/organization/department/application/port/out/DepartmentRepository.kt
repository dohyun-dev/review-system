package com.dohyundev.review.organization.department.application.port.out

import com.dohyundev.review.organization.department.domain.entity.Department
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface DepartmentRepository : JpaRepository<Department, Long> {
    fun findByNameAndParentId(name: String, parentId: Long?): Optional<Department>
}
