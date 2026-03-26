package com.dohyundev.review.organization.member.domain.exception

import java.util.NoSuchElementException

class DepartmentMemberNotFoundException(
    message: String = "부서원이 존재하지 않습니다."
) : NoSuchElementException(message)
