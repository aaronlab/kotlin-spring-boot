package com.kotlinspring.repository

import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining() {
        val courses = courseRepository.findByNameContaining("SpringBoot")

        assert(courses.isNotEmpty())
        assertEquals(
            courseEntityList()
                .filter { it.name.contains("SpringBoot", ignoreCase = true) }
                .size,
            courses.size
        )
    }

    @Test
    fun findCoursesByName() {
        val courses = courseRepository.findCoursesByName("SpringBoot")

        assert(courses.isNotEmpty())
        assertEquals(
            courseEntityList()
                .filter { it.name.contains("SpringBoot", ignoreCase = true) }
                .size,
            courses.size
        )
    }

}