package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

    List<Assessment> findByScoreGreaterThan(int score);
    List<Assessment> findByTakenAtAfter(Instant date);

    @Query("SELECT a FROM Assessment a WHERE a.score > :score")
    List<Assessment> findAssessmentScore(@Param("score") int score);

    @Query("SELECT a FROM Assessment a WHERE a.takenAt > :date")
    List<Assessment> findAssessmentTakenAt(@Param("date") Instant date);

}
