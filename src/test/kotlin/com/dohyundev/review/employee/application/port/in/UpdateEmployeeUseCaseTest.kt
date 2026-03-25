package com.dohyundev.review.employee.application.port.`in`

import com.dohyundev.review.employee.application.port.out.EmployeeRepository
import com.dohyundev.review.employee.application.port.service.EmployeeCommandService
import com.dohyundev.review.employee.domain.dto.UpdateEmployeeCommand
import com.dohyundev.review.employee.domain.entity.EmployeeRole
import com.dohyundev.review.employee.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.employee.fixture.EmployeeFixture
import kr.co.modaoutlet.mgr.employee.domain.EmployeeStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
class UpdateEmployeeUseCaseTest {

    @Autowired
    lateinit var employeeCommandService: EmployeeCommandService

    @Autowired
    lateinit var employeeRepository: EmployeeRepository

    @Test
    fun `사원 정보를 변경한다`() {
        val employee = employeeRepository.save(EmployeeFixture.create(no = "EMP001"))
        val command = UpdateEmployeeCommand(
            name = "변경이름",
            role = EmployeeRole.ADMIN,
            hireDate = LocalDate.of(2025, 6, 1),
            status = EmployeeStatus.ON_LEAVE,
        )

        employeeCommandService.update(employee.id!!, command)

        val found = employeeRepository.findById(employee.id!!).orElseThrow()
        assertEquals("변경이름", found.name)
        assertEquals(EmployeeRole.ADMIN, found.role)
        assertEquals(LocalDate.of(2025, 6, 1), found.hireDate)
        assertEquals(EmployeeStatus.ON_LEAVE, found.status)
    }

    @Test
    fun `존재하지 않는 사원 ID면 EmployeeNotFoundException을 던진다`() {
        val command = UpdateEmployeeCommand(
            name = "이름",
            role = EmployeeRole.USER,
            hireDate = LocalDate.now(),
            status = EmployeeStatus.ACTIVE,
        )

        assertThrows<EmployeeNotFoundException> {
            employeeCommandService.update(999999L, command)
        }
    }

}
