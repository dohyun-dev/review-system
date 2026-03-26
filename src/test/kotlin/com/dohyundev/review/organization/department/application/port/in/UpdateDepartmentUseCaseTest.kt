package com.dohyundev.review.organization.department.application.port.`in`

import com.dohyundev.review.organization.department.application.port.dto.UpdateDepartmentCommand
import com.dohyundev.review.organization.department.application.port.out.DepartmentRepository
import com.dohyundev.review.organization.department.application.port.service.DepartmentCommandService
import com.dohyundev.review.organization.department.domain.exception.DepartmentNotFoundException
import com.dohyundev.review.organization.department.domain.exception.DuplicateDepartmentNameException
import com.dohyundev.review.organization.department.fixture.DepartmentFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UpdateDepartmentUseCaseTest {

    @Autowired
    lateinit var departmentCommandService: DepartmentCommandService

    @Autowired
    lateinit var departmentRepository: DepartmentRepository

    @Test
    fun `부서 정보를 변경한다`() {
        val department = departmentRepository.save(DepartmentFixture.create(name = "개발팀"))
        val parent = departmentRepository.save(DepartmentFixture.create(name = "IT본부"))
        val command = UpdateDepartmentCommand(name = "백엔드팀", parentId = parent.id)

        departmentCommandService.update(department.id!!, command)

        val found = departmentRepository.findById(department.id!!).orElseThrow()
        assertEquals("백엔드팀", found.name)
        assertEquals(parent.id, found.parent?.id)
    }

    @Test
    fun `상위 부서를 제거한다`() {
        val parent = departmentRepository.save(DepartmentFixture.create(name = "IT본부"))
        val department = departmentRepository.save(DepartmentFixture.create(name = "개발팀", parent = parent))
        val command = UpdateDepartmentCommand(name = "개발팀", parentId = null)

        departmentCommandService.update(department.id!!, command)

        val found = departmentRepository.findById(department.id!!).orElseThrow()
        assertNull(found.parent)
    }

    @Test
    fun `존재하지 않는 부서 ID면 DepartmentNotFoundException을 던진다`() {
        val command = UpdateDepartmentCommand(name = "개발팀")

        assertThrows<DepartmentNotFoundException> {
            departmentCommandService.update(999999L, command)
        }
    }

    @Test
    fun `같은 상위 부서 내에 이미 존재하는 이름으로 변경하면 DuplicateDepartmentNameException을 던진다`() {
        val parent = departmentRepository.save(DepartmentFixture.create(name = "IT본부"))
        departmentRepository.save(DepartmentFixture.create(name = "백엔드팀", parent = parent))
        val department = departmentRepository.save(DepartmentFixture.create(name = "개발팀", parent = parent))
        val command = UpdateDepartmentCommand(name = "백엔드팀", parentId = parent.id)

        assertThrows<DuplicateDepartmentNameException> {
            departmentCommandService.update(department.id!!, command)
        }
    }

    @Test
    fun `같은 이름과 같은 상위 부서로 변경하면 중복 예외가 발생하지 않는다`() {
        val parent = departmentRepository.save(DepartmentFixture.create(name = "IT본부"))
        val department = departmentRepository.save(DepartmentFixture.create(name = "개발팀", parent = parent))
        val command = UpdateDepartmentCommand(name = "개발팀", parentId = parent.id)

        departmentCommandService.update(department.id!!, command)

        val found = departmentRepository.findById(department.id!!).orElseThrow()
        assertEquals("개발팀", found.name)
    }
}
