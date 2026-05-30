package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    InstructorRepository instructorRepository;

    private Course course;
    private Instructor instructor;
    private Instant now;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        instructorRepository.deleteAll();
        now = Instant.now();

        Instructor instructor1 = Instructor.builder()
                .fullName("Instructor 1")
                .email("instructor1@email.com")
                .build();

        Course course = Course.builder()
                .title("course 1")
                .active(true)
                .status("activo")
                .createAt(now)
                .build();

    }

    @Test
    @DisplayName("Find course by title")
    void findByTitle() {
        Course course1 = Course.builder()
                .title("course 1")
                .status("activo")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();

        Course course2 = Course.builder()
                .title("course 2")
                .status("activo")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();

        Course course3 = Course.builder()
                .title("course 3")
                .status("activo")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        List<Course> courses = courseRepository.findByTitle("course 1");

        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getTitle()).isEqualTo("course 1");
    }

    @Test
    @DisplayName("Find Course by Status (String)")
    void findByStatus() {

        Course course1 = Course.builder()
                .title("course 1")
                .status("activo")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();

        Course course2 = Course.builder()
                .title("course 2")
                .status("activo")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();

        Course course3 = Course.builder()
                .title("course 3")
                .status("desactivado")
                .active(false)
                .createAt(now)
                .instructor(instructor)
                .build();

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        List<Course> courses = courseRepository.findByStatus("activo");

        assertThat(courses).hasSize(2);
        assertThat(courses.get(0).getStatus()).isEqualTo("activo");

    }

    @Test
    @DisplayName("Find active courses (boolean)")
    void findActiveCourse() {

        Course course1 = Course.builder()
                .title("course desactivado")
                .status("desactivado")
                .active(false)
                .createAt(now)
                .instructor(instructor)
                .build();

        Course course2 = Course.builder()
                .title("course Activo")
                .status("activo")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();


        courseRepository.save(course1);
        courseRepository.save(course2);


        List<Course> courses = courseRepository.findActiveCourse();
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getStatus()).isEqualTo("activo");
    }

    @Test
    @DisplayName("Find course created after a date (beforeDate)")
    void findCourseCreatedAfter() {

        Instant beforeDate = Instant.now().minus(3, ChronoUnit.DAYS);

        Course course1 = Course.builder()
                .title("course 1")
                .status("activo")
                .active(true)
                .createAt(beforeDate)
                .instructor(instructor)
                .build();

        Course course2 = Course.builder()
                .title("course 2")
                .status("activo")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();

        courseRepository.save(course1);
        courseRepository.save(course2);

        List<Course> courses = courseRepository.findCourseCreatedAfter(beforeDate);
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getTitle()).isEqualTo("course 2");

    }

    @Test
    @DisplayName("Find courses by specific status (activo/desactivado")
    void findCourseStatus() {

        Course course1 = Course.builder()
                .title("course 1")
                .status("activo")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();

        Course course2 = Course.builder()
                .title("course 2")
                .status("desactivado")
                .active(true)
                .createAt(now)
                .instructor(instructor)
                .build();

        Course course3 = Course.builder()
                .title("course prueba")
                .status("prueba")
                .active(false)
                .createAt(now)
                .instructor(instructor)
                .build();

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        List<Course> course = courseRepository.findCourseStatus("activo", "desactivado");
        assertThat(course).hasSize(2);
        assertThat(course).extracting(Course::getStatus)
                .containsExactly("activo", "desactivado");
    }
}