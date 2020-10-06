package com.kfaang.main.board

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PostRepository : JpaRepository<Post, Long> {

}