package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDTO
import com.kotlinspring.util.courseEntityList
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse() {
        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseEntityList().first())
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
    fun addCourseValidation() {
        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val courseDTO = CourseDTO(null, "", "")

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            response!!.contains("must not be blank")
        }
    }

    @Test
    fun retrieveCourses() {
        val mockCourseDTOs = listOf(
            courseDTO(id = 1),
            courseDTO(id = 2),
            courseDTO(id = 3)
        )

        every { courseServiceMock.retrieveCourses() }.returnsMany(
            mockCourseDTOs
        )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(mockCourseDTOs.size, courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {
        val course = courseDTO(id = 100)
        val updateCourseDTO = courseDTO(
            id = course.id,
            name = "${course.name} - Updated",
        )

        every { courseServiceMock.updateCourse(any(), any()) } returns updateCourseDTO

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/${course.id}", course.id)
            .bodyValue(updateCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(updateCourseDTO.name, updatedCourse!!.name)
    }

    @Test
    fun deleteCourse() {
        val id = 100

        every { courseServiceMock.deleteCourse(any()) } just runs

        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", id)
            .exchange()
            .expectStatus().isNoContent
    }
}