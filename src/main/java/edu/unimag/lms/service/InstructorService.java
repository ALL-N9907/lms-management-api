package edu.unimag.lms.service;

import edu.unimag.lms.api.dto.InstructorDto.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InstructorService {
    InstructorResponse create(InstructorCreateRequest request);
    InstructorResponse update(UUID id, InstructorUpdateRequest request);
    InstructorResponse findById(UUID id);
    List<InstructorResponse> findAll();
    void delete(UUID id);
    List<InstructorResponse> findByFullName(String fullName);
    List<InstructorResponse> findByEmail(String email);
    List<InstructorResponse> findInstructorCreatedAt(Instant date);
}