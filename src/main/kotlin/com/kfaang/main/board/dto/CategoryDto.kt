package com.kfaang.main.board.dto

import com.kfaang.main.board.Category

class CategoryDto(
    var name: String? = null
) {
    fun toCategory(): Category {
        return Category(name = this.name)
    }
}