package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.CourseDto.*;
import edu.unimag.lms.service.mapper.CourseMapper;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Instructor;
import edu.unimag.lms.domine.Repositories.CourseRepository;
import edu.unimag.lms.domine.Repositories.InstructorRepository;
import edu.unimag.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse create(CourseCreateRequest request) {
        Instructor instructor = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found: " + request.instructorId()));

        Course course = courseMapper.toEntity(request);
        course.setInstructor(instructor);
        course.setCreateAt(Instant.now());

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse update(UUID id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
        Instructor instructor = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found: " + request.instructorId()));

        courseMapper.updateFromRequest(request, course);
        course.setInstructor(instructor);
        course.setUpdateAt(Instant.now());

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse findById(UUID id) {
        return courseRepository.findById(id)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
    }

    @Override
    public List<CourseResponse> findAll() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!courseRepository.existsById(id))
            throw new RuntimeException("Course not found: " + id);
        courseRepository.deleteById(id);
    }

    @Override
    public List<CourseResponse> findByTitle(String title) {
        return courseRepository.findByTitle(title).stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> findByStatus(String status) {
        return courseRepository.findByStatus(status).stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> findActiveCourses() {
        return courseRepository.findActiveCourse().stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> findCoursesCreatedAfter(Instant date) {
        return courseRepository.findCourseCreatedAfter(date).stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> findCoursesByStatuses(String status1, String status2) {
        return courseRepository.findCourseStatus(status1, status2).stream()
                .map(courseMapper::toResponse)
                .toList();
    }
}