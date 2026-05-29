package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.AssessmentDto.*;
import edu.unimag.lms.domine.Entities.Assessment;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Student;
import edu.unimag.lms.domine.Repositories.AssessmentRepository;
import edu.unimag.lms.domine.Repositories.CourseRepository;
import edu.unimag.lms.domine.Repositories.StudentRepository;
import edu.unimag.lms.service.AssessmentService;
import edu.unimag.lms.service.mapper.AssessmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AssessmentMapper assessmentMapper;

    @Override
    public AssesmentResponse create(AssessmentCreateRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + request.courseId()));

        Assessment assessment = assessmentMapper.toEntity(request);
        assessment.setStudent(student);
        assessment.setCourse(course);
        assessment.setTakenAt(Instant.now());

        return assessmentMapper.toResponse(assessmentRepository.save(assessment));
    }

    @Override
    public AssesmentResponse update(UUID id, AssesmentUpdateRequest request) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found: " + id));

        assessmentMapper.updateFromRequest(request, assessment);

        return assessmentMapper.toResponse(assessmentRepository.save(assessment));
    }

    @Override
    public AssesmentResponse findById(UUID id) {
        return assessmentRepository.findById(id)
                .map(assessmentMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Assessment not found: " + id));
    }

    @Override
    public List<AssesmentResponse> findAll() {
        return assessmentRepository.findAll().stream()
                .map(assessmentMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!assessmentRepository.existsById(id))
            throw new RuntimeException("Assessment not found: " + id);
        assessmentRepository.deleteById(id);
    }

    @Override
    public List<AssesmentResponse> findByScoreGreaterThan(int score) {
        return assessmentRepository.findByScoreGreaterThan(score).stream()
                .map(assessmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AssesmentResponse> findByTakenAtAfter(Instant date) {
        return assessmentRepository.findByTakenAtAfter(date).stream()
                .map(assessmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AssesmentResponse> findAssessmentScore(int score) {
        return assessmentRepository.findAssessmentScore(score).stream()
                .map(assessmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AssesmentResponse> findAssessmentTakenAt(Instant date) {
        return assessmentRepository.findAssessmentTakenAt(date).stream()
                .map(assessmentMapper::toResponse)
                .toList();
    }
}