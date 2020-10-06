package com.kfaang.main

import com.kfaang.main.membership.Account
import com.kfaang.main.membership.AccountService
import com.kfaang.main.membership.dto.SignUpDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@SpringBootTest
@AutoConfigureMockMvc
class BaseControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountService: AccountService

    fun getAccessToken(): String {
        val signUpDto = SignUpDto(email = "whdgus8219@naver.com", password = "428563", nickname = "마나얼")
        generateAccount(signUpDto)

        val clientId = "myApp"
        val clientSecret = "pass"

        val perform = mockMvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(clientId, clientSecret))
                .param("username", signUpDto.email)
                .param("password", signUpDto.password)
                .param("grant_type", "password"))

        val responseBody = perform.andReturn().response.contentAsString
        val parser = Jackson2JsonParser()
        return parser.parseMap(responseBody).get("access_token").toString()
    }

    private fun generateAccount(signUpDto: SignUpDto) : Account {
        return accountService.processNewAccount(signUpDto)
    }
}