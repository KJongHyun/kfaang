package com.kfaang.main.board

import com.fasterxml.jackson.databind.ObjectMapper
import com.kfaang.main.AccountRepository
import com.kfaang.main.BaseControllerTests
import com.kfaang.main.board.dto.CategoryDto
import com.kfaang.main.board.dto.WritePostDto
import com.kfaang.main.board.dto.WriteReplyDto
import com.kfaang.main.membership.dto.SignUpDto
import org.junit.jupiter.api.AfterEach
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

    companion object {
        private val BOARD = "/api/board"
    }

    @BeforeEach
    fun beforeEach() {
        postRepository.deleteAll()
        categoryRepository.deleteAll()
        accountRepository.deleteAll()
        val signUpDto = SignUpDto(email = "whdgus8219@naver.com", password = "428563", nickname = "마나얼")
        generateAccount(signUpDto);
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

    @DisplayName("게시글 작성")
    @Test
    fun writePost() {
        val signUpDto = SignUpDto(email = "whdgus8219@naver.com", password = "428563", nickname = "마나얼")
        val account = generateAccount(signUpDto)

        val categoryName = "free"

        val category = boardService.createCategory(Category(name = categoryName), account = account)

        val writePostDto = WritePostDto("타이틀", "컨텐츠", category.id!!)
        val perform = mockMvc.perform(post("$BOARD/${category.name}/write")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(signUpDto.email, signUpDto.password))
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

        val writePostDto = WritePostDto("타이틀", "컨텐츠", category.id!!)

        val post = boardService.writePost(writePostDto = writePostDto, account = account)

        mockMvc.perform(get("$BOARD/${post.id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("contents").exists())
    }

    @DisplayName("댓글 쓰기")
    @Test
    fun writeReply() {
        val categoryName = "free"

        val categoryId = createCategory(categoryName)

        val postId = writePost(categoryId)
        val writeReplyDto = WriteReplyDto(postId = postId, contents = "댓글")

        mockMvc.perform(post("$BOARD/write/reply")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken("whdgus8219@naver.com", "428563"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(writeReplyDto)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("id").exists())
    }

    private fun getBearerToken() = "Bearer ${getAccessToken()}"

    private fun getBearerToken(email: String, password: String) = "Bearer ${getAccessTokenBySignUpDto(email, password)}"

    private fun createCategory(categoryName: String): Long {
        val categoryDto = CategoryDto(name = categoryName)

        val perform = mockMvc.perform(post("/api/board/new-category")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken("whdgus8219@naver.com", "428563"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))

        val responseBody = perform.andReturn().response.contentAsString
        val parser = Jackson2JsonParser()

        return parser.parseMap(responseBody)["id"].toString().toLong()
    }

    private fun writePost(categoryId: Long): Long {

        val writePostDto = WritePostDto("타이틀", "컨텐츠", categoryId)

        val perform = mockMvc.perform(post("$BOARD/write/post")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken("whdgus8219@naver.com", "428563"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(writePostDto)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("id").exists())


        val parser = Jackson2JsonParser()
        val id = parser.parseMap(perform.andReturn().response.contentAsString)["id"].toString()
        return id.toLong()
    }
}