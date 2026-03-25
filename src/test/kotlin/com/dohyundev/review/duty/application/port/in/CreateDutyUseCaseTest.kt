package com.dohyundev.review.duty.application.port.`in`

import com.dohyundev.review.duty.application.port.dto.CreateDutyCommand
import com.dohyundev.review.duty.application.port.out.DutyRepository
import com.dohyundev.review.duty.application.port.service.DutyCommandService
import com.dohyundev.review.duty.domain.DutyStatus
import com.dohyundev.review.duty.domain.exception.DuplicateDutyNameException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CreateDutyUseCaseTest {

    @Autowired
    lateinit var dutyCommandService: DutyCommandService

    @Autowired
    lateinit var dutyRepository: DutyRepository

    @Test
    fun `직책을 저장하고 DB에서 조회할 수 있다`() {
        val command = CreateDutyCommand(name = "팀장")

        val saved = dutyCommandService.create(command)

        val found = dutyRepository.findById(saved.id!!).orElseThrow()
        assertEquals("팀장", found.name)
        assertEquals(DutyStatus.ACTIVE, found.status)
    }

    @Test
    fun `첫 번째 직책의 순서는 1이다`() {
        val command = CreateDutyCommand(name = "팀장")

        val saved = dutyCommandService.create(command)

        assertEquals(1, saved.sortOrder)
    }

    @Test
    fun `두 번째 직책의 순서는 2이다`() {
        dutyCommandService.create(CreateDutyCommand(name = "팀장"))
        val second = dutyCommandService.create(CreateDutyCommand(name = "부장"))

        assertEquals(2, second.sortOrder)
    }

    @Test
    fun `이미 존재하는 직책명으로 생성하면 DuplicateDutyNameException을 던진다`() {
        dutyCommandService.create(CreateDutyCommand(name = "팀장"))

        assertThrows<DuplicateDutyNameException> {
            dutyCommandService.create(CreateDutyCommand(name = "팀장"))
        }
    }
}
