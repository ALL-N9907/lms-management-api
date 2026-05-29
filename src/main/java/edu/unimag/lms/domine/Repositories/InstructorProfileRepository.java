package edu.unimag.lms.domine.Repositories;

import edu.unimag.lms.domine.Entities.Instructor_Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface InstructorProfileRepository extends JpaRepository<Instructor_Profile, Long> {


    List<Instructor_Profile> findByPhone(String phone);

    List<Instructor_Profile> findByBioContaining(String text);

    @Query("SELECT ip FROM Instructor_Profile ip WHERE ip.phone = :phone")
    List<Instructor_Profile> searchByPhone(@Param("phone") String phone);

    @Query("SELECT ip FROM Instructor_Profile ip WHERE ip.instructor.id = :instructorId")
    Instructor_Profile findProfileByInstructor(@Param("instructorId") UUID instructorId);
}
