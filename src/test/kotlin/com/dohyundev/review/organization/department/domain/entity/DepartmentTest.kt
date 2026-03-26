package com.dohyundev.review.organization.department.domain.entity

import com.dohyundev.review.organization.department.fixture.DepartmentFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class DepartmentTest {

    @Test
    fun `create - 이름으로 부서를 생성한다`() {
        val department = Department.create(name = "개발팀")

        assertEquals("개발팀", department.name)
        assertNull(department.parent)
    }

    @Test
    fun `create - 상위 부서를 지정하여 부서를 생성한다`() {
        val parent = DepartmentFixture.create(name = "IT본부")

        val department = Department.create(name = "개발팀", parent = parent)

        assertEquals("개발팀", department.name)
        assertEquals(parent, department.parent)
    }

    @Test
    fun `update - 이름과 상위 부서를 변경한다`() {
        val department = DepartmentFixture.create(name = "개발팀")
        val newParent = DepartmentFixture.create(name = "IT본부")

        department.update(name = "백엔드팀", parent = newParent)

        assertEquals("백엔드팀", department.name)
        assertEquals(newParent, department.parent)
    }

    @Test
    fun `update - 상위 부서를 제거한다`() {
        val parent = DepartmentFixture.create(name = "IT본부")
        val department = DepartmentFixture.create(name = "개발팀", parent = parent)

        department.update(name = "개발팀", parent = null)

        assertNull(department.parent)
    }
}
