package com.dohyundev.review.employee.query.repository

import com.dohyundev.review.employee.command.domain.QEmployee.employee
import com.dohyundev.review.employee.query.model.EmployeeListView
import com.dohyundev.review.employee.query.model.QEmployeeListView
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class EmployeeListQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findAll(pageable: Pageable): Page<EmployeeListView> {
        val projection = QEmployeeListView(
            employee.id,
            employee.no.number,
            employee.name,
            employee.hireDate,
            employee.role,
            employee.status,
        )
        val content = queryFactory
            .select(projection)
            .from(employee)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(employee.count())
            .from(employee)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}
