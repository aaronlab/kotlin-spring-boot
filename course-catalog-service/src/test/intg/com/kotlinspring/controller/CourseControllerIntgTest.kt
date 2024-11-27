package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Build Restfule APIs using SpringBoot and Kotlin", "Programming")

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

    @Test
    fun retrieveAllCourses() {
        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(courseEntityList().size, courseDTOs!!.size)
    }

    @Test
    fun retrieveAllCoursesByName() {
        val courseName = "SpringBoot"

        val uri = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("courseName", courseName)
            .toUriString()

        val courseDTOs = webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            courseEntityList()
                .filter { it.name.contains(courseName, ignoreCase = true) }
                .size,
            courseDTOs!!.size
        )
    }

    @Test
    fun updateCourse() {
        // Existing course
        val course = courseEntityList().first()
        courseRepository.save(course)

        // Update CourseDTO
        val updateCourseDTO = course.copy().let {
            it.name = "${it.name} - Updated"
            it
        }

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/${course.id}")
            .bodyValue(updateCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(updateCourseDTO.name, updatedCourse!!.name)
    }

    @Test
    fun deleteCourse() {
        val course = courseEntityList().first()
        courseRepository.save(course)

        val deletedCourse = webTestClient
            .delete()
            .uri("/v1/courses/${course.id}")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(Void::class.java)
            .returnResult()
            .responseBody

        Assertions.assertNull(deletedCourse)
    }
}