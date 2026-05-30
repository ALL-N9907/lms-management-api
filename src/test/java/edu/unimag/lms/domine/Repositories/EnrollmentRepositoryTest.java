package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Enrollment;
import edu.unimag.lms.domine.Entities.Instructor;
import edu.unimag.lms.domine.Entities.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EnrollmentRepositoryTest extends AbstractRepositoryIT {


    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private Instant now;
    private Student student1;
    private Student student2;
    private Course course;


    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        instructorRepository.deleteAll();
        now = Instant.now();

        Instructor instructor = Instructor.builder()
                .fullName("instructor test")
                .email("instructor@email.com")
                .build();
        instructorRepository.save(instructor);

        student1 = Student.builder()
                .fullName("Student 1")
                .email("student1@email.com")
                .createAt(now)
                .build();

        student2 = Student.builder()
                .fullName("Student 2")
                .email("student2@email.com")
                .createAt(now)
                .build();

        studentRepository.saveAll(List.of(student1,student2));

        course = Course.builder()
                .title("course 1")
                .status("activo")
                .active(true)
                .createAt(now)
                .build();
        courseRepository.save(course);
    }

    @Test
    void existsByStudentId() {
        Enrollment enrollment = Enrollment.builder()
                .student(student1)
                .course(course)
                .status("activo")
                .enrolledAt(now)
                .build();
        enrollmentRepository.save(enrollment);


        assertThat(enrollmentRepository.existsByStudentId(student1.getId())).isTrue();
        assertThat(enrollmentRepository.existsByStudentId(student2.getId())).isFalse();
        assertThat(enrollmentRepository.existsByStudentId(UUID.randomUUID())).isFalse();
    }
    @Test
    void findByStatus() {
        Enrollment enrollment1 = Enrollment.builder()
                .student(student1)
                .course(course)
                .status("activo")
                .enrolledAt(now)
                .build();
        enrollmentRepository.save(enrollment1);

        Enrollment enrollment2 = Enrollment.builder()
                .student(student2)
                .course(course)
                .status("activo")
                .enrolledAt(now)
                .build();
        enrollmentRepository.save(enrollment2);

        Enrollment enrollment3 = Enrollment.builder()
                .student(student1)
                .course(course)
                .status("cancelado")
                .enrolledAt(now)
                .build();
        enrollmentRepository.save(enrollment3);

        List<Enrollment> activeEnrollments = enrollmentRepository.findByStatus("activo");
        List<Enrollment> canceledEnrollments = enrollmentRepository.findByStatus("cancelado");

        assertThat(activeEnrollments.size()).isEqualTo(2);
        assertThat(canceledEnrollments.size()).isEqualTo(1);
        assertThat(activeEnrollments).extracting(Enrollment::getStudent)
                .containsExactly(student1, student2);
    }

    @Test
    @DisplayName("Find enrollment by day enrolled")
    void findEnrollmentEnrolledAt() {
        Instant beforeDate = Instant.now().minus(3, ChronoUnit.DAYS);

        Enrollment enrollment1 = Enrollment.builder()
                .student(student1)
                .course(course)
                .status("activo")
                .enrolledAt(now.plus(1, ChronoUnit.DAYS))
                .build();
        enrollmentRepository.save(enrollment1);

        Enrollment enrollment2 = Enrollment.builder()
                .student(student2)
                .course(course)
                .status("activo")
                .enrolledAt(now)
                .build();
        enrollmentRepository.save(enrollment2);

        Enrollment enrollment3 = Enrollment.builder()
                .student(student1)
                .course(course)
                .status("cancelado")
                .enrolledAt(beforeDate)
                .build();
        enrollmentRepository.save(enrollment3);

        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentEnrolledAt(now);
        Assertions.assertThat(enrollments.size()).isEqualTo(1);
        assertThat(enrollments.get(0)).isEqualTo(enrollment2);
    }
}