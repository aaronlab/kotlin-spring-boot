package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService
) {
    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {

        val instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId!!)

        if (!instructorOptional.isPresent) {
            throw InstructorNotValidException("Instructor not valid for the id: ${courseDTO.instructorId}")
        }

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructorOptional.get())
        }

        courseRepository.save(courseEntity)

        logger.info("Saved course is: $courseEntity")


        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun retrieveCourses(courseName: String?): List<CourseDTO> {

        val courses = courseName?.let {
            courseRepository.findCoursesByName(it)
        } ?: courseRepository.findAll()

        return courses.map {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun updateCourse(id: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRepository.findById(id)

        return if (existingCourse.isPresent) {
            existingCourse.get().let {
                it.name = courseDTO.name
                it.category = courseDTO.category
                courseRepository.save(it)

                CourseDTO(it.id, it.name, it.category)
            }
        } else {
            throw CourseNotFoundException("No course found for the passed in id: $id")
        }
    }

    fun deleteCourse(id: Int) {
        val existingCourse = courseRepository.findById(id)

        if (existingCourse.isPresent) {
            courseRepository.delete(existingCourse.get())
        } else {
            throw CourseNotFoundException("No course found for the passed in id: $id")
        }
    }
}