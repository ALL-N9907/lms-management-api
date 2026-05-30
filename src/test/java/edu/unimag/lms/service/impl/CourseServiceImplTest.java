package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.CourseDto;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Instructor;
import edu.unimag.lms.domine.Repositories.CourseRepository;
import edu.unimag.lms.domine.Repositories.InstructorRepository;
import edu.unimag.lms.service.mapper.CourseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private CourseMapper courseMapper;

    private Course course;
    private Instructor instructor;
    private Instant now;
    private UUID courseId;
    private UUID instructorId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        instructorId = UUID.randomUUID();
        now = Instant.now();

        instructor = Instructor.builder()
                .id(instructorId)
                .fullName("Instructor Test")
                .email("instructor@test.com")
                .createAt(now)
                .updateAt(now)
                .build();

        course = Course.builder()
                .id(courseId)
                .title("Course Test")
                .status("ACTIVO")
                .active(true)
                .createAt(now)
                .updateAt(now)
                .instructor(instructor)
                .build();
    }

    @Test
    void create() {
        CourseDto.CourseCreateRequest createRequest = new CourseDto.CourseCreateRequest(
                "New Course",
                "ACTIVO",
                instructorId,
                true
        );

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseMapper.toEntity(createRequest)).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDto.CourseResponse expectedResponse = new CourseDto.CourseResponse(
                courseId,
                instructorId,
                "Course Test",
                "ACTIVO",
                true,
                0,
                List.of(),
                now
        );
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);

        CourseDto.CourseResponse result = courseService.create(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Course Test");
        assertThat(result.Status()).isEqualTo("ACTIVO");

        verify(instructorRepository).findById(instructorId);
        verify(courseMapper).toEntity(createRequest);
        verify(courseRepository).save(any(Course.class));
        verify(courseMapper).toResponse(course);
    }

    @Test
    void update() {
        CourseDto.CourseUpdateRequest updateRequest = new CourseDto.CourseUpdateRequest(
                "Updated Course",
                "INACTIVO",
                false,
                instructorId
        );

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        Course updatedCourse = Course.builder()
                .id(courseId)
                .title("Updated Course")
                .status("INACTIVO")
                .active(false)
                .createAt(now)
                .updateAt(now)
                .instructor(instructor)
                .build();

        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        CourseDto.CourseResponse expectedResponse = new CourseDto.CourseResponse(
                courseId,
                instructorId,
                "Updated Course",
                "INACTIVO",
                false,
                0,
                List.of(),
                now
        );
        when(courseMapper.toResponse(updatedCourse)).thenReturn(expectedResponse);

        CourseDto.CourseResponse result = courseService.update(courseId, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Updated Course");
        assertThat(result.Status()).isEqualTo("INACTIVO");
        assertThat(result.active()).isFalse();

        verify(courseRepository).findById(courseId);
        verify(instructorRepository).findById(instructorId);
        verify(courseRepository).save(any(Course.class));
        verify(courseMapper).toResponse(updatedCourse);
    }

    @Test
    void findById() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        CourseDto.CourseResponse expectedResponse = new CourseDto.CourseResponse(
                courseId, instructorId, "Course Test", "ACTIVO", true, 0, List.of(), now
        );
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);

        CourseDto.CourseResponse result = courseService.findById(courseId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(courseId);
        assertThat(result.title()).isEqualTo("Course Test");

        verify(courseRepository).findById(courseId);
        verify(courseMapper).toResponse(course);
    }

    @Test
    void findAll() {
        Course course2 = Course.builder()
                .id(UUID.randomUUID())
                .title("Course 2")
                .status("ACTIVO")
                .active(true)
                .createAt(now)
                .updateAt(now)
                .instructor(instructor)
                .build();

        List<Course> courses = List.of(course, course2);

        when(courseRepository.findAll()).thenReturn(courses);

        CourseDto.CourseResponse response1 = new CourseDto.CourseResponse(
                courseId, instructorId, "Course Test", "ACTIVO", true, 0, List.of(), now
        );
        CourseDto.CourseResponse response2 = new CourseDto.CourseResponse(
                course2.getId(), instructorId, "Course 2", "ACTIVO", true, 0, List.of(), now
        );

        when(courseMapper.toResponse(course)).thenReturn(response1);
        when(courseMapper.toResponse(course2)).thenReturn(response2);

        List<CourseDto.CourseResponse> result = courseService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(CourseDto.CourseResponse::title)
                .containsExactlyInAnyOrder("Course Test", "Course 2");

        verify(courseRepository).findAll();
        verify(courseMapper, times(2)).toResponse(any(Course.class));
    }

    @Test
    void delete() {
        when(courseRepository.existsById(courseId)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(courseId);

        courseService.delete(courseId);

        verify(courseRepository).existsById(courseId);
        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void findByTitle() {
        List<Course> courses = List.of(course);

        when(courseRepository.findByTitle("Course Test")).thenReturn(courses);

        CourseDto.CourseResponse response = new CourseDto.CourseResponse(
                courseId, instructorId, "Course Test", "ACTIVO", true, 0, List.of(), now
        );
        when(courseMapper.toResponse(course)).thenReturn(response);

        List<CourseDto.CourseResponse> result = courseService.findByTitle("Course Test");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Course Test");

        verify(courseRepository).findByTitle("Course Test");
        verify(courseMapper).toResponse(course);
    }

    @Test
    void findByStatus() {
        List<Course> courses = List.of(course);

        when(courseRepository.findByStatus("ACTIVO")).thenReturn(courses);

        CourseDto.CourseResponse response = new CourseDto.CourseResponse(
                courseId, instructorId, "Course Test", "ACTIVO", true, 0, List.of(), now
        );
        when(courseMapper.toResponse(course)).thenReturn(response);

        List<CourseDto.CourseResponse> result = courseService.findByStatus("ACTIVO");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).Status()).isEqualTo("ACTIVO");

        verify(courseRepository).findByStatus("ACTIVO");
        verify(courseMapper).toResponse(course);
    }


    @Test
    void findActiveCourses() {
        List<Course> courses = List.of(course);

        when(courseRepository.findActiveCourse()).thenReturn(courses);

        CourseDto.CourseResponse response = new CourseDto.CourseResponse(
                courseId, instructorId, "Course Test", "ACTIVO", true, 0, List.of(), now
        );
        when(courseMapper.toResponse(course)).thenReturn(response);

        List<CourseDto.CourseResponse> result = courseService.findActiveCourses();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).active()).isTrue();

        verify(courseRepository).findActiveCourse();
        verify(courseMapper).toResponse(course);
    }

    @Test
    void findCoursesCreatedAfter() {
        Instant pastDate = now.minus(10, ChronoUnit.DAYS);
        List<Course> courses = List.of(course);

        when(courseRepository.findCourseCreatedAfter(pastDate)).thenReturn(courses);

        CourseDto.CourseResponse response = new CourseDto.CourseResponse(
                courseId, instructorId, "Course Test", "ACTIVO", true, 0, List.of(), now
        );
        when(courseMapper.toResponse(course)).thenReturn(response);

        List<CourseDto.CourseResponse> result = courseService.findCoursesCreatedAfter(pastDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).createdAt()).isAfter(pastDate);

        verify(courseRepository).findCourseCreatedAfter(pastDate);
        verify(courseMapper).toResponse(course);
    }

    @Test
    void findCoursesByStatuses() {
        List<Course> courses = List.of(course);

        when(courseRepository.findCourseStatus("ACTIVO", "PENDIENTE")).thenReturn(courses);

        CourseDto.CourseResponse response = new CourseDto.CourseResponse(
                courseId, instructorId, "Course Test", "ACTIVO", true, 0, List.of(), now
        );
        when(courseMapper.toResponse(course)).thenReturn(response);

        List<CourseDto.CourseResponse> result = courseService.findCoursesByStatuses("ACTIVO", "PENDIENTE");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).Status()).isEqualTo("ACTIVO");

        verify(courseRepository).findCourseStatus("ACTIVO", "PENDIENTE");
        verify(courseMapper).toResponse(course);
    }
}