package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LessonRepositoryTest extends AbstractRepositoryIT{

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course course1;
    private Course course2;
    private Lesson lesson1;
    private Lesson lesson2;
    private Instant now;

    @BeforeEach
    void setup() {

        lessonRepository.deleteAll();
        courseRepository.deleteAll();
        now = Instant.now();

        course1 = Course.builder()
                .title("Course 1")
                .status("activo")
                .active(true)
                .build();

        courseRepository.save(course1);

        course2 = Course.builder()
                .title("Special Course 2")
                .status("activo")
                .active(true)
                .build();

        courseRepository.save(course2);

        lesson1 = Lesson.builder()
                .course(course1)
                .title("Regular Lesson 1")
                .orderIndex(1)
                .build();

        lesson2 = Lesson.builder()
                .course(course2)
                .title("Special Lesson 2")
                .orderIndex(2)
                .build();

        lessonRepository.saveAll(List.of(lesson1, lesson2));
    }

    @Test
    void findByTitleContaining() {
        List<Lesson> lessons = lessonRepository.findByTitleStartingWith("Special");
        assertThat(lessons).hasSize(1);
        assertThat(lessons.get(0).getId()).isEqualTo(lesson2.getId());
    }

    @Test
    void findByTitleStartingWith() {
        List<Lesson> lessons = lessonRepository.findByTitleStartingWith("Regular");
        assertThat(lessons).hasSize(1);
        assertThat(lessons.get(0).getId()).isEqualTo(lesson1.getId());
    }

    @Test
    @DisplayName("Find lesson by course")
    void findLessonsByCourse() {
        List<Lesson> lessons = lessonRepository.findLessonsByCourse(course1.getId());
        assertThat(lessons).hasSize(1);
        assertThat(lessons.get(0).getId()).isEqualTo(lesson1.getId());

    }

    @Test
    @DisplayName("find lessons after Order a")
    void findLessonsAfterOrder() {
        Lesson lesson3 = Lesson.builder()
                .course(course1)
                .title("Extra lesson 3")
                .orderIndex(3)
                .build();
        lessonRepository.save(lesson3);

        List<Lesson> lessons = lessonRepository.findLessonsAfterOrder(1);
        assertThat(lessons).hasSize(2);
        assertThat(lessons).extracting(Lesson::getOrderIndex)
                .containsExactlyInAnyOrder(2, 3);
    }
}