package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentId(UUID studentId);
    List<Enrollment> findByStatus (String status);

    @Query("SELECT e FROM Enrollment e WHERE e.enrolledAt > :date")
    List<Enrollment> findEnrollmentEnrolledAt(@Param("date")Instant date);
}
