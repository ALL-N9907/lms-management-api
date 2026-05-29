package edu.unimag.lms.service;

import edu.unimag.lms.api.dto.StudentDto.*;
import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentResponse create(StudentCreateRequest request);
    StudentResponse update(UUID id, StudentUpdateRequest request);
    StudentResponse findById(UUID id);
    List<StudentResponse> findAll();
    void delete(UUID id);
    List<StudentResponse> findByFullName(String fullName);
    List<StudentResponse> findByEmail(String email);
    List<StudentResponse> findStudentsCreatedAfter(java.time.Instant date);
    List<StudentResponse> findStudentsByNameLike(String name);
}