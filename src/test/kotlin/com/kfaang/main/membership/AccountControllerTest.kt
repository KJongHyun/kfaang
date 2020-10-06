package com.kfaang.main.membership

import com.fasterxml.jackson.databind.ObjectMapper
import com.kfaang.main.AccountRepository
import com.kfaang.main.board.PostRepository
import com.kfaang.main.membership.dto.SignUpDto
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
internal class AccountControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var postRepository: PostRepository

    @BeforeEach
    fun initData() {
        postRepository.deleteAll()
        accountRepository.deleteAll()
    }

    @DisplayName("회원가입")
    @Test
    fun signUp() {
        val signUpdDto = SignUpDto(email = "whdgus8219@naver.com", password = "428563", nickname = "마나얼")
        mockMvc.perform(post("/api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpdDto)))
                .andDo(print())
                .andExpect(status().isCreated)

        val account = accountRepository.findByEmail(signUpdDto.email)
        assertNotNull(account)
        assertEquals(signUpdDto.email, account?.email)
        assertNotEquals(signUpdDto.password, account?.password)
    }

    @DisplayName("토큰 발행 테스트")
    @Test
    fun getAuthToken() {

        val clientId = "myApp"
        val clientSecret = "pass"

        val signUpDto = SignUpDto(email = "whdgus8219@naver.com", password = "428563", nickname = "마나얼")
        generateAccount(signUpDto)

        mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", signUpDto.email)
                .param("password", signUpDto.password)
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("access_token").exists())
    }


    private fun generateAccount(signUpDto: SignUpDto) : Account {
        return accountService.processNewAccount(signUpDto)
    }

}