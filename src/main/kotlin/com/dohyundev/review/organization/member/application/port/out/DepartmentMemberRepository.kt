package com.dohyundev.review.organization.member.application.port.out

import com.dohyundev.review.organization.member.domain.entity.DepartmentMember
import com.dohyundev.review.organization.member.domain.entity.DepartmentMemberType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface DepartmentMemberRepository : JpaRepository<DepartmentMember, Long> {
    fun findByDepartmentIdAndEmployeeId(departmentId: Long, employeeId: Long): Optional<DepartmentMember>
    fun findByEmployeeIdAndType(employeeId: Long, type: DepartmentMemberType): Optional<DepartmentMember>
}
