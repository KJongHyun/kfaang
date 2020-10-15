package com.kfaang.main.board

import com.fasterxml.jackson.databind.ObjectMapper
import com.kfaang.main.AccountRepository
import com.kfaang.main.BaseControllerTests
import com.kfaang.main.board.dto.CategoryDto
import com.kfaang.main.board.dto.WritePostDto
import com.kfaang.main.membership.dto.SignUpDto
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class BoardControllerTest : BaseControllerTests() {

    @Autowired lateinit var objectMapper: ObjectMapper

    @Autowired lateinit var accountRepository: AccountRepository

    @Autowired lateinit var postRepository: PostRepository

    @Autowired lateinit var categoryRepository: CategoryRepository

    @Autowired lateinit var boardService: BoardService

    @BeforeEach
    fun beforeEach() {
        postRepository.deleteAll()
        categoryRepository.deleteAll()
        accountRepository.deleteAll()
    }

    @DisplayName("게시글 작성")
    @Test
    fun writePost() {
        val signUpDto = SignUpDto(email = "whdgus8219@naver.com", password = "428563", nickname = "마나얼")
        val account = generateAccount(signUpDto)

        val categoryName = "free"

        val category = boardService.createCategory(Category(name = categoryName), account = account)

        val writePostDto = WritePostDto("타이틀", "컨텐츠")
        val perform = mockMvc.perform(post("/api/board/${category.name}/write")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(signUpDto))
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

    @DisplayName("게시글 보기")
    @Test
    fun getPost() {
        val signUpDto = SignUpDto(email = "whdgus8219@naver.com", password = "428563", nickname = "마나얼")
        val account = generateAccount(signUpDto)



        val categoryName = "free"

        val category = boardService.createCategory(Category(name = categoryName), account = account)

        val writePostDto = WritePostDto("타이틀", "컨텐츠")

        val post = boardService.writePost(writePostDto = writePostDto, category = category, account = account)

        mockMvc.perform(get("/api/board/${post.id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("contents").exists())
    }

    @DisplayName("카테고리 생성")
    @Test
    fun createCategory() {
        val categoryDto = CategoryDto(name = "question")

        val perform = mockMvc.perform(post("/api/board/new-category")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("id").exists())

        val parser = Jackson2JsonParser()
        val id = parser.parseMap(perform.andReturn().response.contentAsString)["id"].toString()
        val category = categoryRepository.findById(id.toLong()).orElse(null)
        assertNotNull(category)
        assertEquals(categoryDto.name, category.name)


    }

    private fun getBearerToken() = "Bearer ${getAccessToken()}"

    private fun getBearerToken(signUpDto: SignUpDto) = "Bearer ${getAccessTokenBySignUpDto(signUpDto)}"
}