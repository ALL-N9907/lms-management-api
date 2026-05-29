package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.StudentDto.*;
import edu.unimag.lms.domine.Entities.Student;
import edu.unimag.lms.domine.Repositories.StudentRepository;
import edu.unimag.lms.service.StudentService;
import edu.unimag.lms.service.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentResponse create(StudentCreateRequest request) {
        Student student = studentMapper.toEntity(request);
        student.setCreateAt(Instant.now());

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse update(UUID id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));

        studentMapper.updateFromRequest(request, student);
        student.setUpdateAt(Instant.now());

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse findById(UUID id) {
        return studentRepository.findById(id)
                .map(studentMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));
    }

    @Override
    public List<StudentResponse> findAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!studentRepository.existsById(id))
            throw new RuntimeException("Student not found: " + id);
        studentRepository.deleteById(id);
    }

    @Override
    public List<StudentResponse> findByFullName(String fullName) {
        return studentRepository.findByFullName(fullName).stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> findByEmail(String email) {
        return studentRepository.findByEmail(email).stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> findStudentsCreatedAfter(Instant date) {
        return studentRepository.findStudentsCreatedAfter(date).stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> findStudentsByNameLike(String name) {
        return studentRepository.findStudentsByNameLike(name).stream()
                .map(studentMapper::toResponse)
                .toList();
    }
}