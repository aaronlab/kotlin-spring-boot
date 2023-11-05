package me.aaronlee.blog.repository

import me.aaronlee.blog.entity.Feed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository : JpaRepository<Feed, Long> {
}