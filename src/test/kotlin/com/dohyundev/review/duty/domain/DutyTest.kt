package com.dohyundev.review.duty.domain

import com.dohyundev.review.duty.domain.dto.UpdateDutyCommand
import com.dohyundev.review.duty.fixture.DutyFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DutyTest {

    @Test
    fun `create - 이름과 순서로 직책을 생성한다`() {
        val duty = Duty.create(name = "팀장", sortOrder = 1)

        assertEquals("팀장", duty.name)
        assertEquals(1, duty.sortOrder)
    }

    @Test
    fun `create - 신규 직책의 기본 상태는 ACTIVE이다`() {
        val duty = Duty.create(name = "팀장", sortOrder = 1)

        assertEquals(DutyStatus.ACTIVE, duty.status)
    }

    @Test
    fun `update - 이름, 순서, 상태를 변경한다`() {
        val duty = DutyFixture.create(name = "팀장", sortOrder = 1, status = DutyStatus.ACTIVE)
        val command = UpdateDutyCommand(name = "부장", sortOrder = 2, status = DutyStatus.INACTIVE)

        duty.update(command)

        assertEquals("부장", duty.name)
        assertEquals(2, duty.sortOrder)
        assertEquals(DutyStatus.INACTIVE, duty.status)
    }

    @Test
    fun `swapOrder - 두 직책의 순서를 교환한다`() {
        val duty1 = DutyFixture.create(name = "팀장", sortOrder = 1)
        val duty2 = DutyFixture.create(name = "부장", sortOrder = 2)

        duty1.swapOrder(duty2)

        assertEquals(2, duty1.sortOrder)
        assertEquals(1, duty2.sortOrder)
    }
}
