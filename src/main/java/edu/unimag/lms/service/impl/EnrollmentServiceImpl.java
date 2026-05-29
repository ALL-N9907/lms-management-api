package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.EnrollmentDto.*;
import edu.unimag.lms.service.mapper.EnrollmentMapper;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Enrollment;
import edu.unimag.lms.domine.Entities.Student;
import edu.unimag.lms.domine.Repositories.CourseRepository;
import edu.unimag.lms.domine.Repositories.EnrollmentRepository;
import edu.unimag.lms.domine.Repositories.StudentRepository;
import edu.unimag.lms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public EnrollmentResponse create(EnrollmentCreateRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + request.courseId()));

        Enrollment enrollment = enrollmentMapper.toEntity(request);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(Instant.now());

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponse update(UUID id, EnrollmentUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found: " + id));

        enrollmentMapper.updateFromRequest(request, enrollment);

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponse findById(UUID id) {
        return enrollmentRepository.findById(id)
                .map(enrollmentMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Enrollment not found: " + id));
    }

    @Override
    public List<EnrollmentResponse> findAll() {
        return enrollmentRepository.findAll().stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!enrollmentRepository.existsById(id))
            throw new RuntimeException("Enrollment not found: " + id);
        enrollmentRepository.deleteById(id);
    }

    @Override
    public boolean existsByStudentId(UUID studentId) {
        return enrollmentRepository.existsByStudentId(studentId);
    }

    @Override
    public List<EnrollmentResponse> findByStatus(String status) {
        return enrollmentRepository.findByStatus(status).stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<EnrollmentResponse> findEnrollmentEnrolledAt(Instant date) {
        return enrollmentRepository.findEnrollmentEnrolledAt(date).stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }
}