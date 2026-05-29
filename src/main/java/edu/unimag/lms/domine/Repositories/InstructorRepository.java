package edu.unimag.lms.domine.Repositories;


import edu.unimag.lms.domine.Entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    List<Instructor> findByFullName (String fullname);
    List<Instructor> findByEmail (String Email);

    @Query("SELECT i FROM Instructor i WHERE i.createAt > :date")
    List<Instructor> findInstructorCreatedAt(@Param("date")Instant date);
}
