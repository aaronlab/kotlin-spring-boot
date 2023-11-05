package me.aaronlee.blog.controller

import me.aaronlee.blog.dto.FeedBodyDto
import me.aaronlee.blog.entity.Feed
import me.aaronlee.blog.service.FeedService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("feed")
class FeedController @Autowired constructor(
    val feedService: FeedService
) {
    @PostMapping
    fun createFeed(@RequestBody body: FeedBodyDto): ResponseEntity<Feed> {
        val feed = feedService.save(body)
        return ResponseEntity.ok().body(feed)
    }

    @GetMapping("/list")
    fun listFeed(): ResponseEntity<List<Feed>> {
        return ResponseEntity.ok().body(feedService.getFeedList())
    }

    @GetMapping("/{id}")
    fun getFeed(@PathVariable id: Long): ResponseEntity<Feed> {
        val feed = feedService.getFeed(id)
        return ResponseEntity.ok().body(feed)
    }

    @DeleteMapping("/{id}")
    fun deleteFeed(@PathVariable id: Long): ResponseEntity<Boolean> {
        feedService.deleteFeed(id)
        return ResponseEntity.ok().body(true)
    }

    @PutMapping("/{id}")
    fun updateFeed(
        @PathVariable id: Long,
        body: FeedBodyDto
    ): ResponseEntity<Feed> {
        val feed = feedService.updateFeed(id, body)
        return ResponseEntity.ok().body(feed)
    }
}