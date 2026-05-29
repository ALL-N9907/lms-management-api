package edu.unimag.lms.domine.Repositories;


import edu.unimag.lms.domine.Entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByTitleContaining(String title);
    List<Lesson> findByTitleStartingWith(String title);

    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId")
    List<Lesson> findLessonsByCourse(@Param("courseId") UUID courseId);

    @Query("SELECT l FROM Lesson l WHERE l.orderIndex > :index")
    List<Lesson> findLessonsAfterOrder(@Param("index") Integer index);
}
