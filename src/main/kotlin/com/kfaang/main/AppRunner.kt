package com.kfaang.main

import com.kfaang.main.membership.AccountService
import com.kfaang.main.membership.dto.SignUpDto
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AppRunner(
        private val accountService: AccountService
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {

    }
}