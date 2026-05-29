package edu.unimag.lms.service;

import edu.unimag.lms.api.dto.LessonDto.*;
import java.util.List;
import java.util.UUID;

public interface LessonService {
    LessonResponse create(LessonCreateRequest request);
    LessonResponse update(UUID id, LessonUpdateRequest request);
    LessonResponse findById(UUID id);
    List<LessonResponse> findAll();
    void delete(UUID id);
    List<LessonResponse> findByTitleContaining(String title);
    List<LessonResponse> findByTitleStartingWith(String prefix);
    List<LessonResponse> findLessonsByCourse(UUID courseId);
    List<LessonResponse> findLessonsAfterOrder(int orderIndex);
}