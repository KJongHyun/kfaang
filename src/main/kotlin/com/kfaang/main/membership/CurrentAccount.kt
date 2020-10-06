package com.kfaang.main.membership

import org.springframework.security.core.annotation.AuthenticationPrincipal


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
annotation class CurrentAccount {
}