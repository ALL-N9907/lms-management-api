package edu.unimag.lms.service;

import edu.unimag.lms.api.dto.AssessmentDto.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssessmentService {
    AssesmentResponse create(AssessmentCreateRequest request);
    AssesmentResponse update(UUID id, AssesmentUpdateRequest request);
    AssesmentResponse findById(UUID id);
    List<AssesmentResponse> findAll();
    void delete(UUID id);
    List<AssesmentResponse> findByScoreGreaterThan(int score);
    List<AssesmentResponse> findByTakenAtAfter(Instant date);
    List<AssesmentResponse> findAssessmentScore(int score);
    List<AssesmentResponse> findAssessmentTakenAt(Instant date);
}