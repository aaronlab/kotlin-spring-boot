package com.kotlinspring.repository

import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream
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

        assertEquals(
            courseEntityList()
                .filter { it.name.contains("SpringBoot", ignoreCase = true) }
                .size,
            courses.size
        )
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findCoursesByNameApproach2(name: String, expectedSize: Int) {
        val courses = courseRepository.findCoursesByName(name)

        assertEquals(expectedSize, courses.size)
    }

    companion object {

        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments
                    .arguments(
                        "SpringBoot",
                        courseEntityList()
                            .filter { it.name.contains("SpringBoot", ignoreCase = true) }
                            .size
                    ),
                Arguments
                    .arguments(
                        "Wiremock",
                        courseEntityList()
                            .filter { it.name.contains("Wiremock", ignoreCase = true) }
                            .size
                    )
            )
        }

    }

}