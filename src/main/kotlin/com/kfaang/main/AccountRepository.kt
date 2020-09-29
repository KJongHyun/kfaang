package com.kfaang.main

import com.kfaang.main.membership.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun existsByEmail(email: String?): Boolean
    fun existsByNickname(nickname: String?): Boolean
    fun findByEmail(email: String): Account?
}