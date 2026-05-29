package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

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
