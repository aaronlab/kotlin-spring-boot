package me.aaronlee.blog.service

import me.aaronlee.blog.dto.FeedBodyDto
import me.aaronlee.blog.entity.Feed
import me.aaronlee.blog.repository.FeedRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FeedService @Autowired constructor(
    val feedRepository: FeedRepository,
    val modelMapper: ModelMapper
) {
    fun save(body: FeedBodyDto): Feed? {
        return feedRepository.save(
            modelMapper.map(
                body, Feed::class.java
            )
        )
    }

    fun getFeed(id: Long): Feed = feedRepository.findById(id).get()

    fun deleteFeed(id: Long) {
        feedRepository.deleteById(id)
    }

    fun updateFeed(id: Long, body: FeedBodyDto): Feed {
        val feed = getFeed(id)
        feed.title = body.title
        feed.content = body.content

        feedRepository.save(feed)

        return feed
    }

    fun getFeedList(): List<Feed> {
        return feedRepository.findAll()
    }
}