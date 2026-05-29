package edu.unimag.lms.repositories;

import edu.unimag.lms.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTitle(String title);
    List<Course> findByStatus(String Status);

    @Query("SELECT c FROM Course c WHERE c.active = true")
    List<Course> findActiveCourse();

    @Query("SELECT c FROM Course c WHERE c.createAt > :date")
    List<Course> findCourseCreatedAfter(@Param("date") Instant date);

    @Query("SELECT c FROM Course c WHERE c.status = :activo or c.status = :desactivado")
    List<Course> findCourseStatus(
            @Param("activo") String activo,
            @Param("desactivado") String desactivado
    );


}
