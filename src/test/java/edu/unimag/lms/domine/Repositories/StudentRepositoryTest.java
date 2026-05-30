package edu.unimag.lms.domine.Repositories;

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

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private StudentRepository studentRepository;

    private Student student1;
    private Student student2;
    private Student student3;
    private Instant now;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        now = Instant.now();

        student1 = Student.builder()
                .fullName("Juan Perez")
                .email("juanstudent@email.com")
                .createAt(now.minus(4, ChronoUnit.DAYS))
                .build();

        student2 = Student.builder()
                .fullName("Lucas Perez")
                .email("perez123@email.com")
                .createAt(now)
                .build();

        student3 = Student.builder()
                .fullName("Jose Rodriguez")
                .email("lmsstudent@email.com")
                .createAt(now.minus(2, ChronoUnit.DAYS))
                .build();

        studentRepository.saveAll(List.of(student1, student2, student3));
    }

    @Test
    @DisplayName("find student by full name")
    void findByFullName() {
        List<Student> students = studentRepository.findByFullName("Juan Perez");
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getFullName()).isEqualTo("Juan Perez");
    }

    @Test
    @DisplayName("find student by email")
    void findByEmail() {
        List<Student> students = studentRepository.findByEmail("lmsstudent@email.com");
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getFullName()).isEqualTo("Jose Rodriguez");
    }

    @Test
    @DisplayName("find student by date created after a")
    void findStudentsCreatedAfter() {
        List<Student> students = studentRepository
                .findStudentsCreatedAfter(student1.getCreateAt());
        assertThat(students).hasSize(2);
        assertThat(students).extracting(Student::getFullName)
                .containsExactlyInAnyOrder("Lucas Perez", "Jose Rodriguez");
    }

    @Test
    void findStudentsByNameLike() {
        List<Student> students = studentRepository.findStudentsByNameLike("%Perez%");
        assertThat(students).hasSize(2);
        assertThat(students).extracting(Student::getFullName)
                .containsExactlyInAnyOrder("Lucas Perez", "Juan Perez");
    }
}