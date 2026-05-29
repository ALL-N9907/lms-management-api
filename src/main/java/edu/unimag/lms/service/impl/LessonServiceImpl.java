package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.LessonDto.*;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Lesson;
import edu.unimag.lms.domine.Repositories.CourseRepository;
import edu.unimag.lms.domine.Repositories.LessonRepository;
import edu.unimag.lms.service.LessonService;
import edu.unimag.lms.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    @Override
    public LessonResponse create(LessonCreateRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + request.courseId()));

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    public LessonResponse update(UUID id, LessonUpdateRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found: " + id));

        lessonMapper.updateFromRequest(request, lesson);

        // courseId es opcional en el update
        if (request.courseId() != null) {
            Course course = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + request.courseId()));
            lesson.setCourse(course);
        }

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    public LessonResponse findById(UUID id) {
        return lessonRepository.findById(id)
                .map(lessonMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Lesson not found: " + id));
    }

    @Override
    public List<LessonResponse> findAll() {
        return lessonRepository.findAll().stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!lessonRepository.existsById(id))
            throw new RuntimeException("Lesson not found: " + id);
        lessonRepository.deleteById(id);
    }

    @Override
    public List<LessonResponse> findByTitleContaining(String title) {
        return lessonRepository.findByTitleContaining(title).stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    public List<LessonResponse> findByTitleStartingWith(String prefix) {
        return lessonRepository.findByTitleStartingWith(prefix).stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    public List<LessonResponse> findLessonsByCourse(UUID courseId) {
        return lessonRepository.findLessonsByCourse(courseId).stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    public List<LessonResponse> findLessonsAfterOrder(int orderIndex) {
        return lessonRepository.findLessonsAfterOrder(orderIndex).stream()
                .map(lessonMapper::toResponse)
                .toList();
    }
}