package com.kotlinspring.util

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor

fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(
        null,
        "Build RestFul APIs using SpringBoot and Kotlin",
        "Development",
        instructor
    ),
    Course(
        null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot",
        "Development",
        instructor
    ),
    Course(
        null,
        "Wiremock for Java Developers",
        "Development",
        instructor
    )
)

fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APIs using SpringBoot and Kotlin",
    category: String = "Development"
) = CourseDTO(
    id,
    name,
    category
)

fun instructorEntityList() = listOf(
    Instructor(
        null,
        "John Doe"
    ),
    Instructor(
        null,
        "Jane Doe"
    ),
    Instructor(
        null,
        "John Smith"
    )
)

fun instructorDTO(
    id: Int? = null,
    name: String = "John Doe"
) = InstructorDTO(
    id,
    name
)

fun instructorEntity(name: String = "John Doe") = Instructor(null, name)