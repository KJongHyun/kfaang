package com.kfaang.main.membership

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User


class UserAccount(val account: Account) : User(account.email, account.password, account.roles.map {
    SimpleGrantedAuthority("ROLE_${it.name}")
}.toMutableSet()) {

}