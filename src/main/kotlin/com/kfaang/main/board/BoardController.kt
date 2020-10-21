package com.kfaang.main.board

import com.kfaang.main.board.dto.CategoryDto
import com.kfaang.main.board.dto.WritePostDto
import com.kfaang.main.board.dto.WriteReplyDto
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

    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long): ResponseEntity<Any> {
        val post = boardService.getPost(postId)

        return ResponseEntity(post, HttpStatus.OK)
    }

    @PostMapping("/write/post")
    fun writePost(@RequestBody writePostDto: WritePostDto, @CurrentAccount account: Account): ResponseEntity<Any> {
        val post = boardService.writePost(writePostDto, account)

        return ResponseEntity(post, HttpStatus.OK)
    }

    @PostMapping("/write/reply")
    fun writeReply(@RequestBody writeReplyDto: WriteReplyDto, @CurrentAccount account: Account): ResponseEntity<Any> {
        val reply = boardService.writeReply(writeReplyDto, account)
        return ResponseEntity(reply.id, HttpStatus.CREATED)
    }

    @PostMapping("/new-category")
    fun createCategory(@RequestBody categoryDto: CategoryDto, @CurrentAccount account: Account): ResponseEntity<Any> {

        val category = boardService.createCategory(categoryDto.toCategory(), account)

        return ResponseEntity(category, HttpStatus.CREATED)
    }



}