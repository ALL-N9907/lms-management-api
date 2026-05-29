package edu.unimag.lms.service;

import edu.unimag.lms.api.dto.EnrollmentDto.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    EnrollmentResponse create(EnrollmentCreateRequest request);
    EnrollmentResponse update(UUID id, EnrollmentUpdateRequest request);
    EnrollmentResponse findById(UUID id);
    List<EnrollmentResponse> findAll();
    void delete(UUID id);
    boolean existsByStudentId(UUID studentId);
    List<EnrollmentResponse> findByStatus(String status);
    List<EnrollmentResponse> findEnrollmentEnrolledAt(Instant date);
}