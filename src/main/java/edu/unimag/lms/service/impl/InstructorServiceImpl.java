package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.InstructorDto.*;
import edu.unimag.lms.domine.Entities.Instructor;
import edu.unimag.lms.domine.Repositories.InstructorRepository;
import edu.unimag.lms.service.InstructorService;
import edu.unimag.lms.service.mapper.InstructorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

    @Override
    public InstructorResponse create(InstructorCreateRequest request) {
        Instructor instructor = instructorMapper.toEntity(request);
        instructor.setCreateAt(Instant.now());

        return instructorMapper.toResponse(instructorRepository.save(instructor));
    }

    @Override
    public InstructorResponse update(UUID id, InstructorUpdateRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found: " + id));

        instructorMapper.updateFromRequest(request, instructor);
        instructor.setUpdateAt(Instant.now());

        return instructorMapper.toResponse(instructorRepository.save(instructor));
    }

    @Override
    public InstructorResponse findById(UUID id) {
        return instructorRepository.findById(id)
                .map(instructorMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Instructor not found: " + id));
    }

    @Override
    public List<InstructorResponse> findAll() {
        return instructorRepository.findAll().stream()
                .map(instructorMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!instructorRepository.existsById(id))
            throw new RuntimeException("Instructor not found: " + id);
        instructorRepository.deleteById(id);
    }

    @Override
    public List<InstructorResponse> findByFullName(String fullName) {
        return instructorRepository.findByFullName(fullName).stream()
                .map(instructorMapper::toResponse)
                .toList();
    }

    @Override
    public List<InstructorResponse> findByEmail(String email) {
        return instructorRepository.findByEmail(email).stream()
                .map(instructorMapper::toResponse)
                .toList();
    }

    @Override
    public List<InstructorResponse> findInstructorCreatedAt(Instant date) {
        return instructorRepository.findInstructorCreatedAt(date).stream()
                .map(instructorMapper::toResponse)
                .toList();
    }
}