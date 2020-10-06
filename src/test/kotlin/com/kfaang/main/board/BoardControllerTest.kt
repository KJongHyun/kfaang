package com.kfaang.main.board

import com.fasterxml.jackson.databind.ObjectMapper
import com.kfaang.main.AccountRepository
import com.kfaang.main.BaseControllerTests
import com.kfaang.main.board.dto.WritePostDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class BoardControllerTest : BaseControllerTests() {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var postRepository: PostRepository

    @BeforeEach
    fun beforeEach() {
        postRepository.deleteAll()
        accountRepository.deleteAll()

    }

    @DisplayName("게시글 작성 테스트")
    @Test
    fun writePost() {
        val writePostDto = WritePostDto("타이틀", "컨텐츠")
        val perform = mockMvc.perform(post("/api/board/write")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${getAccessToken()}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(writePostDto)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("id").exists())


        val parser = Jackson2JsonParser()
        val id = parser.parseMap(perform.andReturn().response.contentAsString)["id"].toString()
        val post = postRepository.findById(id.toLong()).orElse(null)
        assertNotNull(post)
        assertEquals(writePostDto.title, post.title)

    }


}