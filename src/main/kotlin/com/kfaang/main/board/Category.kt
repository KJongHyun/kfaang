package com.kfaang.main.board

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Category {
    @Id @GeneratedValue
    var id: Long? = null
}