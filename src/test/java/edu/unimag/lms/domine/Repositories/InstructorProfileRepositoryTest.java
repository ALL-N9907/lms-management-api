package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Instructor;
import edu.unimag.lms.domine.Entities.Instructor_Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InstructorProfileRepositoryTest extends AbstractRepositoryIT{

    @Autowired
    private InstructorProfileRepository instructorProfileRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private Instructor instructor;
    private Instructor_Profile profile;
    private Instant now;
    @BeforeEach
    void setup(){
        instructorRepository.deleteAll();
        instructorRepository.deleteAll();

        now = Instant.now();

        instructor = Instructor.builder()
                .fullName("Instructor")
                .email("instructor@email.com")
                .createAt(now)
                .updateAt(now)
                .build();
        instructorRepository.save(instructor);

        profile = Instructor_Profile.builder()
                .bio("instructor bio")
                .phone("123456789")
                .instructor(instructor)
                .build();

        instructorProfileRepository.save(profile);

    }

    @Test
    @DisplayName("Find instructor profile by phone number")
    void findByPhone() {
        List<Instructor_Profile> profiles = instructorProfileRepository.findByPhone("123456789");
        assertThat(profiles).hasSize(1);
        assertThat(profiles.get(0).getId()).isEqualTo(profile.getId());
    }

    @Test
    @DisplayName("Find instructor profile by containing word")
    void findByBioContaining() {
        List<Instructor_Profile> profiles  =
                instructorProfileRepository.findByBioContaining("instructor");
        assertThat(profiles).hasSize(1);
        assertThat(profiles.get(0).getId()).isEqualTo(profile.getId());
    }

    @Test
    @DisplayName("search profile by phone")
    void searchByPhone() {
        List<Instructor_Profile>  profiles = instructorProfileRepository.findByPhone("123456789");
        assertThat(profiles).hasSize(1);
    }

    @Test
    @DisplayName("find instructor profile by instructor")
    void findProfileByInstructor() {
        List<Instructor_Profile>  profiles =
                Collections.singletonList(
                        instructorProfileRepository.
                                findProfileByInstructor(instructor.getId()));
        assertThat(profiles.get(0).getId()).isEqualTo(profile.getId());
    }
}