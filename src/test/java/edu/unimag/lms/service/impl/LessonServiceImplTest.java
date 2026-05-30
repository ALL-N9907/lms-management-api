package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.LessonDto;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Lesson;
import edu.unimag.lms.domine.Repositories.CourseRepository;
import edu.unimag.lms.domine.Repositories.LessonRepository;
import edu.unimag.lms.service.mapper.LessonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @InjectMocks
    private LessonServiceImpl lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonMapper lessonMapper;

    private Lesson lesson;
    private Course course;
    private UUID lessonId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        lessonId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        course = Course.builder()
                .id(courseId)
                .title("Course Test")
                .status("ACTIVO")
                .active(true)
                .build();

        lesson = Lesson.builder()
                .id(lessonId)
                .title("Lesson 1")
                .orderIndex(1)
                .course(course)
                .build();
    }

    @Test
    void create() {
        LessonDto.LessonCreateRequest createRequest = new LessonDto.LessonCreateRequest(
                courseId,
                "New Lesson",
                2
        );

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonMapper.toEntity(createRequest)).thenReturn(lesson);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        LessonDto.LessonResponse expectedResponse = new LessonDto.LessonResponse(
                lessonId, "Lesson 1", 1, courseId
        );
        when(lessonMapper.toResponse(lesson)).thenReturn(expectedResponse);

        LessonDto.LessonResponse result = lessonService.create(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Lesson 1");

        verify(courseRepository).findById(courseId);
        verify(lessonMapper).toEntity(createRequest);
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void update() {
        LessonDto.LessonUpdateRequest updateRequest = new LessonDto.LessonUpdateRequest(
                "Updated Lesson",
                3,
                courseId
        );

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Lesson updatedLesson = Lesson.builder()
                .id(lessonId)
                .title("Updated Lesson")
                .orderIndex(3)
                .course(course)
                .build();

        when(lessonRepository.save(any(Lesson.class))).thenReturn(updatedLesson);

        LessonDto.LessonResponse expectedResponse = new LessonDto.LessonResponse(
                lessonId, "Updated Lesson", 3, courseId
        );
        when(lessonMapper.toResponse(updatedLesson)).thenReturn(expectedResponse);

        LessonDto.LessonResponse result = lessonService.update(lessonId, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Updated Lesson");
        assertThat(result.orderIndex()).isEqualTo(3);

        verify(lessonRepository).findById(lessonId);
        verify(courseRepository).findById(courseId);
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void findById() {
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        LessonDto.LessonResponse expectedResponse = new LessonDto.LessonResponse(
                lessonId, "Lesson 1", 1, courseId
        );
        when(lessonMapper.toResponse(lesson)).thenReturn(expectedResponse);

        LessonDto.LessonResponse result = lessonService.findById(lessonId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(lessonId);

        verify(lessonRepository).findById(lessonId);
    }

    @Test
    void findAll() {
        Lesson lesson2 = Lesson.builder()
                .id(UUID.randomUUID())
                .title("Lesson 2")
                .orderIndex(2)
                .course(course)
                .build();

        List<Lesson> lessons = List.of(lesson, lesson2);

        when(lessonRepository.findAll()).thenReturn(lessons);

        LessonDto.LessonResponse response1 = new LessonDto.LessonResponse(
                lessonId, "Lesson 1", 1, courseId
        );
        LessonDto.LessonResponse response2 = new LessonDto.LessonResponse(
                lesson2.getId(), "Lesson 2", 2, courseId
        );

        when(lessonMapper.toResponse(lesson)).thenReturn(response1);
        when(lessonMapper.toResponse(lesson2)).thenReturn(response2);

        List<LessonDto.LessonResponse> result = lessonService.findAll();

        assertThat(result).hasSize(2);

        verify(lessonRepository).findAll();
    }

    @Test
    void delete() {
        when(lessonRepository.existsById(lessonId)).thenReturn(true);
        doNothing().when(lessonRepository).deleteById(lessonId);

        lessonService.delete(lessonId);

        verify(lessonRepository).existsById(lessonId);
        verify(lessonRepository).deleteById(lessonId);
    }

    @Test
    void findByTitleContaining() {
        List<Lesson> lessons = List.of(lesson);

        when(lessonRepository.findByTitleContaining("Lesson")).thenReturn(lessons);

        LessonDto.LessonResponse response = new LessonDto.LessonResponse(
                lessonId, "Lesson 1", 1, courseId
        );
        when(lessonMapper.toResponse(lesson)).thenReturn(response);

        List<LessonDto.LessonResponse> result = lessonService.findByTitleContaining("Lesson");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).contains("Lesson");

        verify(lessonRepository).findByTitleContaining("Lesson");
    }

    @Test
    void findByTitleStartingWith() {
        List<Lesson> lessons = List.of(lesson);

        when(lessonRepository.findByTitleStartingWith("Lesson")).thenReturn(lessons);

        LessonDto.LessonResponse response = new LessonDto.LessonResponse(
                lessonId, "Lesson 1", 1, courseId
        );
        when(lessonMapper.toResponse(lesson)).thenReturn(response);

        List<LessonDto.LessonResponse> result = lessonService.findByTitleStartingWith("Lesson");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).startsWith("Lesson");

        verify(lessonRepository).findByTitleStartingWith("Lesson");
    }

    @Test
    void findLessonsByCourse() {
        List<Lesson> lessons = List.of(lesson);

        when(lessonRepository.findLessonsByCourse(courseId)).thenReturn(lessons);

        LessonDto.LessonResponse response = new LessonDto.LessonResponse(
                lessonId, "Lesson 1", 1, courseId
        );
        when(lessonMapper.toResponse(lesson)).thenReturn(response);

        List<LessonDto.LessonResponse> result = lessonService.findLessonsByCourse(courseId);

        assertThat(result).hasSize(1);

        verify(lessonRepository).findLessonsByCourse(courseId);
    }

    @Test
    void findLessonsAfterOrder() {
        List<Lesson> lessons = List.of(lesson);

        when(lessonRepository.findLessonsAfterOrder(0)).thenReturn(lessons);

        LessonDto.LessonResponse response = new LessonDto.LessonResponse(
                lessonId, "Lesson 1", 1, courseId
        );
        when(lessonMapper.toResponse(lesson)).thenReturn(response);

        List<LessonDto.LessonResponse> result = lessonService.findLessonsAfterOrder(0);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).orderIndex()).isGreaterThan(0);

        verify(lessonRepository).findLessonsAfterOrder(0);
    }
}