package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InstructorRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private InstructorRepository instructorRepository;

    Instructor instructor1;
    Instructor instructor2;
    Instant now;

    @BeforeEach
    void setUp() {
        instructorRepository.deleteAll();
        now = Instant.now();

        instructor1 = Instructor.builder()
                .fullName("Instructor Name")
                .email("instructor123@email.com")
                .createAt(now)
                .updateAt(now)
                .build();
        instructorRepository.save(instructor1);

        instructor2 = Instructor.builder()
                .fullName("Instructor Two")
                .email("instructor2420@email.com")
                .createAt(now.minus(3, ChronoUnit.DAYS))
                .updateAt(now)
                .build();
        instructorRepository.save(instructor2);

    }
    @Test
    @DisplayName("Find instructor by name")
    void findByFullName() {
        List<Instructor> instructors = instructorRepository.findByFullName("Instructor Name");
        assertThat(instructors).hasSize(1);
        assertThat(instructors.get(0).getId()).isEqualTo(instructor1.getId());
    }

    @Test
    @DisplayName("Find instructor by ID")
    void findByEmail() {
        List<Instructor> instructors = instructorRepository.findByEmail("instructor2420@email.com");
        assertThat(instructors).hasSize(1);
        assertThat(instructors.get(0).getId()).isEqualTo(instructor2.getId());
    }

    @Test
    @DisplayName("Find instructor by date createdAt")
    void findInstructorCreatedAt() {
        List<Instructor> instructors = instructorRepository.findInstructorCreatedAt(now);
        assertThat(instructors).hasSize(1);
        assertThat(instructors.get(0).getId()).isEqualTo(instructor1.getId());
    }
}