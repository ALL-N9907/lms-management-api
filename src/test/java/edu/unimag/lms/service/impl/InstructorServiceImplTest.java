package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.InstructorDto;
import edu.unimag.lms.domine.Entities.Instructor;
import edu.unimag.lms.domine.Repositories.InstructorRepository;
import edu.unimag.lms.service.mapper.InstructorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceImplTest {

    @InjectMocks
    private InstructorServiceImpl instructorService;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private InstructorMapper instructorMapper;

    private Instructor instructor;
    private Instant now;
    private UUID instructorId;

    @BeforeEach
    void setUp() {
        instructorId = UUID.randomUUID();
        now = Instant.now();

        instructor = Instructor.builder()
                .id(instructorId)
                .fullName("John Doe")
                .email("john.doe@example.com")
                .createAt(now)
                .updateAt(now)
                .build();
    }

    @Test
    void create() {
        InstructorDto.InstructorCreateRequest createRequest = new InstructorDto.InstructorCreateRequest(
                "jane.smith@example.com",
                "Jane Smith"
        );

        when(instructorMapper.toEntity(createRequest)).thenReturn(instructor);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        InstructorDto.InstructorResponse expectedResponse = new InstructorDto.InstructorResponse(
                instructorId, "john.doe@example.com", "John Doe", now, now
        );
        when(instructorMapper.toResponse(instructor)).thenReturn(expectedResponse);

        InstructorDto.InstructorResponse result = instructorService.create(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo("John Doe");
        assertThat(result.email()).isEqualTo("john.doe@example.com");

        verify(instructorMapper).toEntity(createRequest);
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void update() {
        InstructorDto.InstructorUpdateRequest updateRequest = new InstructorDto.InstructorUpdateRequest(
                "john.updated@example.com"
        );

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        Instructor updatedInstructor = Instructor.builder()
                .id(instructorId)
                .fullName("John Doe")
                .email("john.updated@example.com")
                .createAt(now)
                .updateAt(now)
                .build();

        when(instructorRepository.save(any(Instructor.class))).thenReturn(updatedInstructor);

        InstructorDto.InstructorResponse expectedResponse = new InstructorDto.InstructorResponse(
                instructorId, "john.updated@example.com", "John Doe", now, now
        );
        when(instructorMapper.toResponse(updatedInstructor)).thenReturn(expectedResponse);

        InstructorDto.InstructorResponse result = instructorService.update(instructorId, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("john.updated@example.com");

        verify(instructorRepository).findById(instructorId);
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void findById() {
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        InstructorDto.InstructorResponse expectedResponse = new InstructorDto.InstructorResponse(
                instructorId, "john.doe@example.com", "John Doe", now, now
        );
        when(instructorMapper.toResponse(instructor)).thenReturn(expectedResponse);

        InstructorDto.InstructorResponse result = instructorService.findById(instructorId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(instructorId);
        assertThat(result.fullName()).isEqualTo("John Doe");

        verify(instructorRepository).findById(instructorId);
    }

    @Test
    void findAll() {
        Instructor instructor2 = Instructor.builder()
                .id(UUID.randomUUID())
                .fullName("Jane Smith")
                .email("jane@example.com")
                .createAt(now)
                .updateAt(now)
                .build();

        List<Instructor> instructors = List.of(instructor, instructor2);

        when(instructorRepository.findAll()).thenReturn(instructors);

        InstructorDto.InstructorResponse response1 = new InstructorDto.InstructorResponse(
                instructorId, "john.doe@example.com", "John Doe", now, now
        );
        InstructorDto.InstructorResponse response2 = new InstructorDto.InstructorResponse(
                instructor2.getId(), "jane@example.com", "Jane Smith", now, now
        );

        when(instructorMapper.toResponse(instructor)).thenReturn(response1);
        when(instructorMapper.toResponse(instructor2)).thenReturn(response2);

        List<InstructorDto.InstructorResponse> result = instructorService.findAll();

        assertThat(result).hasSize(2);

        verify(instructorRepository).findAll();
    }

    @Test
    void delete() {
        when(instructorRepository.existsById(instructorId)).thenReturn(true);
        doNothing().when(instructorRepository).deleteById(instructorId);

        instructorService.delete(instructorId);

        verify(instructorRepository).existsById(instructorId);
        verify(instructorRepository).deleteById(instructorId);
    }

    @Test
    void findByFullName() {
        List<Instructor> instructors = List.of(instructor);

        when(instructorRepository.findByFullName("John Doe")).thenReturn(instructors);

        InstructorDto.InstructorResponse response = new InstructorDto.InstructorResponse(
                instructorId, "john.doe@example.com", "John Doe", now, now
        );
        when(instructorMapper.toResponse(instructor)).thenReturn(response);

        List<InstructorDto.InstructorResponse> result = instructorService.findByFullName("John Doe");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).fullName()).isEqualTo("John Doe");

        verify(instructorRepository).findByFullName("John Doe");
    }

    @Test
    void findByEmail() {
        List<Instructor> instructors = List.of(instructor);

        when(instructorRepository.findByEmail("john.doe@example.com")).thenReturn(instructors);

        InstructorDto.InstructorResponse response = new InstructorDto.InstructorResponse(
                instructorId, "john.doe@example.com", "John Doe", now, now
        );
        when(instructorMapper.toResponse(instructor)).thenReturn(response);

        List<InstructorDto.InstructorResponse> result = instructorService.findByEmail("john.doe@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo("john.doe@example.com");

        verify(instructorRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void findInstructorCreatedAt() {
        Instant pastDate = now.minus(10, ChronoUnit.DAYS);
        List<Instructor> instructors = List.of(instructor);

        when(instructorRepository.findInstructorCreatedAt(pastDate)).thenReturn(instructors);

        InstructorDto.InstructorResponse response = new InstructorDto.InstructorResponse(
                instructorId, "john.doe@example.com", "John Doe", now, now
        );
        when(instructorMapper.toResponse(instructor)).thenReturn(response);

        List<InstructorDto.InstructorResponse> result = instructorService.findInstructorCreatedAt(pastDate);

        assertThat(result).hasSize(1);

        verify(instructorRepository).findInstructorCreatedAt(pastDate);
    }
}