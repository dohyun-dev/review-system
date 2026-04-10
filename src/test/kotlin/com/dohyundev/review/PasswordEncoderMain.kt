package com.dohyundev.review

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun main() {
    print("인코딩할 비밀번호 입력: ")
    val raw = readlnOrNull() ?: return
    val encoded = BCryptPasswordEncoder().encode(raw)
    println("인코딩 결과: $encoded")
}