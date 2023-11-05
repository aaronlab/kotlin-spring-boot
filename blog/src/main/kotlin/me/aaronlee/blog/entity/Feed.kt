package me.aaronlee.blog.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Feed(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var createdBy: String,
    var title: String,
    var content: String
)