package com.dohyundev.review.employee.adapter.out

import com.dohyundev.review.employee.domain.entity.Password
import com.dohyundev.review.employee.domain.service.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class BcryptPasswordEncoderAdapter : PasswordEncoder {
    private val bcryptPasswordEncoder = BCryptPasswordEncoder()

    override fun encode(rawPassword: String): Password {
        return Password(bcryptPasswordEncoder.encode(rawPassword)!!)
    }

    override fun matches(rawPassword: String?, encodedPassword: String): Boolean {
        return bcryptPasswordEncoder.matches(rawPassword, encodedPassword)
    }
}
