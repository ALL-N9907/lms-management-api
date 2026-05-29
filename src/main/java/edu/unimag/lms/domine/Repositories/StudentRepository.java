package edu.unimag.lms.domine.Repositories;


import edu.unimag.lms.domine.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    List<Student> findByFullName(String fullName);
    List<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.createAt > :date")
    List<Student> findStudentsCreatedAfter(@Param("date") Instant date);
    @Query("SELECT s FROM Student s WHERE s.fullName LIKE %:name%")
    List<Student> findStudentsByNameLike(@Param("name") String name);
}
