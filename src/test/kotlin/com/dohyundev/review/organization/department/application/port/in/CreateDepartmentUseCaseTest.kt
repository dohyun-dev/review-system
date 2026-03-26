package com.dohyundev.review.organization.department.application.port.`in`

import com.dohyundev.review.organization.department.application.port.dto.CreateDepartmentCommand
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
class CreateDepartmentUseCaseTest {

    @Autowired
    lateinit var departmentCommandService: DepartmentCommandService

    @Autowired
    lateinit var departmentRepository: DepartmentRepository

    @Test
    fun `부서를 저장하고 DB에서 조회할 수 있다`() {
        val command = CreateDepartmentCommand(name = "개발팀")

        val saved = departmentCommandService.create(command)

        val found = departmentRepository.findById(saved.id!!).orElseThrow()
        assertEquals("개발팀", found.name)
        assertNull(found.parent)
    }

    @Test
    fun `상위 부서를 지정하여 부서를 생성한다`() {
        val parent = departmentRepository.save(DepartmentFixture.create(name = "IT본부"))
        val command = CreateDepartmentCommand(name = "개발팀", parentId = parent.id)

        val saved = departmentCommandService.create(command)

        val found = departmentRepository.findById(saved.id!!).orElseThrow()
        assertEquals("개발팀", found.name)
        assertEquals(parent.id, found.parent?.id)
    }

    @Test
    fun `같은 상위 부서 내에 동일한 이름으로 생성하면 DuplicateDepartmentNameException을 던진다`() {
        val parent = departmentRepository.save(DepartmentFixture.create(name = "IT본부"))
        departmentCommandService.create(CreateDepartmentCommand(name = "개발팀", parentId = parent.id))

        assertThrows<DuplicateDepartmentNameException> {
            departmentCommandService.create(CreateDepartmentCommand(name = "개발팀", parentId = parent.id))
        }
    }

    @Test
    fun `다른 상위 부서 아래에는 같은 이름으로 생성할 수 있다`() {
        val parent1 = departmentRepository.save(DepartmentFixture.create(name = "IT본부"))
        val parent2 = departmentRepository.save(DepartmentFixture.create(name = "경영본부"))
        departmentCommandService.create(CreateDepartmentCommand(name = "개발팀", parentId = parent1.id))

        val saved = departmentCommandService.create(CreateDepartmentCommand(name = "개발팀", parentId = parent2.id))

        assertEquals("개발팀", saved.name)
        assertEquals(parent2.id, saved.parent?.id)
    }

    @Test
    fun `존재하지 않는 상위 부서 ID를 지정하면 DepartmentNotFoundException을 던진다`() {
        val command = CreateDepartmentCommand(name = "개발팀", parentId = 999999L)

        assertThrows<DepartmentNotFoundException> {
            departmentCommandService.create(command)
        }
    }
}
