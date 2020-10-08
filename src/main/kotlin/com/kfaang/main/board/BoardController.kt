package com.kfaang.main.board

import com.kfaang.main.board.dto.CategoryDto
import com.kfaang.main.board.dto.WritePostDto
import com.kfaang.main.membership.Account
import com.kfaang.main.membership.CurrentAccount
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/board")
class BoardController(
        private val categoryRepository: CategoryRepository,
        private val boardService: BoardService
) {

    @PostMapping("/{categoryName}/write")
    fun writePost(@RequestBody writePostDto: WritePostDto, @PathVariable categoryName: String, @CurrentAccount account: Account): ResponseEntity<Any> {
        val category = categoryRepository.findByName(categoryName) ?: throw Exception()
        val post = boardService.writePost(writePostDto, category, account)

        return ResponseEntity(post, HttpStatus.OK)
    }

    @PostMapping("/new-category")
    fun createCategory(@RequestBody categoryDto: CategoryDto, @CurrentAccount account: Account): ResponseEntity<Any> {

        val category = boardService.createCategory(categoryDto.toCategory(), account)

        return ResponseEntity(category, HttpStatus.OK)
    }

}