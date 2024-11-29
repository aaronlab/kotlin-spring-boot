package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@Testcontainers
class CourseControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    companion object {

        @Container
        val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
            withDatabaseName("testdb")
            withUsername("postgres")
            withPassword("secret")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
            registry.add("spring.datasource.username", postgresDB::getUsername)
            registry.add("spring.datasource.password", postgresDB::getPassword)
        }
    }


    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val instructorEntity = instructorRepository.findAll().first()

        val courseDTO = CourseDTO(
            null,
            "Build Restfule APIs using SpringBoot and Kotlin",
            "Programming",
            instructorEntity.id
        )

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

        val instructorEntity = instructorRepository.findAll().first()

        // Existing course
        val course = Course(
            null,
            "Build RestFul APIs using SpringBoot and Kotlin",
            "Development",
            instructorEntity
        )
        courseRepository.save(course)

        // Update CourseDTO
        val updateCourseDTO = CourseDTO(
            course.id,
            "Build RestFul APIs using SpringBoot and Kotlin",
            "Programming",
            instructorEntity.id
        )

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
        val instructorEntity = instructorRepository.findAll().first()

        val course = Course(
            null,
            "Build RestFul APIs using SpringBoot and Kotlin",
            "Development",
            instructorEntity
        )

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