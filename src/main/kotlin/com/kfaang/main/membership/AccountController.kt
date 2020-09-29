package com.kfaang.main.membership

import com.kfaang.main.membership.dto.SignUpDto
import com.kfaang.main.membership.validator.SignUpDtoValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AccountController(
        private val accountService: AccountService,
        private val signUpDtoValidator: SignUpDtoValidator
) {

    @InitBinder("signUpDto")
    private fun initBinder(webDataBinder: WebDataBinder) {
        webDataBinder.addValidators(signUpDtoValidator)
    }

    @PostMapping("/api/sign-up")
    fun signUp(@RequestBody @Valid signUpDto: SignUpDto, errors: Errors) : ResponseEntity<Any> {
        if (errors.hasErrors())
            return badRequest(errors)
        accountService.processNewAccount(signUpDto)
        return ResponseEntity(HttpStatus.CREATED)
    }

    private fun badRequest(errors: Errors) : ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(errors)
    }
}