package edu.unimag.lms.service;

import edu.unimag.lms.api.dto.CourseDto.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseResponse create(CourseCreateRequest request);
    CourseResponse update(UUID id, CourseUpdateRequest request);
    CourseResponse findById(UUID id);
    List<CourseResponse> findAll();
    void delete(UUID id);
    List<CourseResponse> findByTitle(String title);
    List<CourseResponse> findByStatus(String status);
    List<CourseResponse> findActiveCourses();
    List<CourseResponse> findCoursesCreatedAfter(Instant date);
    List<CourseResponse> findCoursesByStatuses(String status1, String status2);
}