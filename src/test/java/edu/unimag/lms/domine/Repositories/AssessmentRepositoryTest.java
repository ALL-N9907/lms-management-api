package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Assessment;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Instructor;
import edu.unimag.lms.domine.Entities.Student;
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
class AssessmentRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Instant now;
    private Course course;
    private Student student;

    @BeforeEach
    void setUp() {
        assessmentRepository.deleteAll();
        studentRepository.deleteAll();
        instructorRepository.deleteAll();
        courseRepository.deleteAll();
        now = Instant.now();

        Instructor instructor = Instructor.builder()
                .fullName("Instructor Test")
                .email("instructortest@email.com")
                .build();
        instructorRepository.save(instructor);

        Student student = Student.builder()
                .fullName("Student Test")
                .email("studenttest@email.com")
                .createAt(now)
                .build();

        Course course = Course.builder()
                .title("Course Test")
                .status("Active")
                .active(true)
                .instructor(instructor)
                .build();
        courseRepository.save(course);
    }

    @Test
    @DisplayName("Find assessment by score greater than")
    void findByScoreGreaterThan() {
        Assessment assessment1 = Assessment.builder()
                .type("Assessment")
                .score(80)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        Assessment assessment2 = Assessment.builder()
                .type("Assessment")
                .score(60)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        Assessment assessment3 = Assessment.builder()
                .type("Assessment")
                .score(70)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);

        List<Assessment> assessments = assessmentRepository.findByScoreGreaterThan(60);

        assertThat(assessments).hasSize(2);
        assertThat(assessments).extracting(Assessment::getScore)
                .containsExactlyInAnyOrder(80, 70);

    }

    @Test
    @DisplayName("Find assessment taken after instant")
    void findByTakenAtAfter() {
        Instant pastDate = now.minus(3, ChronoUnit.DAYS);

        Assessment assessment1 = Assessment.builder()
                .type("Assessment")
                .score(80)
                .course(course)
                .student(student)
                .takenAt(pastDate)
                .build();

        Assessment assessment2 = Assessment.builder()
                .type("Assessment")
                .score(60)
                .course(course)
                .student(student)
                .takenAt(pastDate)
                .build();

        Assessment assessment3 = Assessment.builder()
                .type("Assessment")
                .score(70)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);

        List<Assessment> found = assessmentRepository.findByTakenAtAfter(pastDate);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getScore()).isEqualTo(70);
    }

    @Test
    @DisplayName("Find assessment by score")
    void findAssessmentScore() {
        Assessment assessment1 = Assessment.builder()
                .type("Assessment")
                .score(80)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        Assessment assessment2 = Assessment.builder()
                .type("Assessment")
                .score(60)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        Assessment assessment3 = Assessment.builder()
                .type("Assessment")
                .score(70)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);

        List<Assessment> found = assessmentRepository.findAssessmentScore(70);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getScore()).isEqualTo(70);
    }

    @Test
    @DisplayName("Find assessment by date Taken")
    void findAssessmentTakenAt() {
        Instant beforeDate = now.minus(3, ChronoUnit.DAYS);
        Assessment assessment1 = Assessment.builder()
                .type("Assessment")
                .score(80)
                .course(course)
                .student(student)
                .takenAt(beforeDate)
                .build();

        Assessment assessment2 = Assessment.builder()
                .type("Assessment")
                .score(60)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        Assessment assessment3 = Assessment.builder()
                .type("Assessment")
                .score(70)
                .course(course)
                .student(student)
                .takenAt(beforeDate)
                .build();

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);

        List<Assessment> found = assessmentRepository.findAssessmentTakenAt(now);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getScore()).isEqualTo(60);
    }
}