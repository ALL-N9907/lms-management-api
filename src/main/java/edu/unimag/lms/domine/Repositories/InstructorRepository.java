package edu.unimag.lms.domine.Repositories;


import edu.unimag.lms.domine.Entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
    List<Instructor> findByFullName (String fullname);
    List<Instructor> findByEmail (String Email);

    @Query("SELECT i FROM Instructor i WHERE i.createAt > :date")
    List<Instructor> findInstructorCreatedAt(@Param("date")Instant date);
}
