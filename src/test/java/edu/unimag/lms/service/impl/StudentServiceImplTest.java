package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.StudentDto;
import edu.unimag.lms.domine.Entities.Student;
import edu.unimag.lms.domine.Repositories.StudentRepository;
import edu.unimag.lms.service.mapper.StudentMapper;
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
class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    private Student student;
    private Instant now;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        now = Instant.now();

        student = Student.builder()
                .id(studentId)
                .fullName("John Doe")
                .email("john.doe@example.com")
                .createAt(now)
                .updateAt(now)
                .build();
    }

    @Test
    void create() {
        StudentDto.StudentCreateRequest createRequest = new StudentDto.StudentCreateRequest(
                "jane.smith@example.com",
                "Jane Smith"
        );

        when(studentMapper.toEntity(createRequest)).thenReturn(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDto.StudentResponse expectedResponse = new StudentDto.StudentResponse(
                studentId, "john.doe@example.com", "John Doe", now, now
        );
        when(studentMapper.toResponse(student)).thenReturn(expectedResponse);

        StudentDto.StudentResponse result = studentService.create(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo("John Doe");
        assertThat(result.email()).isEqualTo("john.doe@example.com");

        verify(studentMapper).toEntity(createRequest);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void update() {
        StudentDto.StudentUpdateRequest updateRequest = new StudentDto.StudentUpdateRequest(
                "john.updated@example.com"
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Student updatedStudent = Student.builder()
                .id(studentId)
                .fullName("John Doe")
                .email("john.updated@example.com")
                .createAt(now)
                .updateAt(now)
                .build();

        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        StudentDto.StudentResponse expectedResponse = new StudentDto.StudentResponse(
                studentId, "john.updated@example.com", "John Doe", now, now
        );
        when(studentMapper.toResponse(updatedStudent)).thenReturn(expectedResponse);

        StudentDto.StudentResponse result = studentService.update(studentId, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("john.updated@example.com");

        verify(studentRepository).findById(studentId);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void findById() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        StudentDto.StudentResponse expectedResponse = new StudentDto.StudentResponse(
                studentId, "john.doe@example.com", "John Doe", now, now
        );
        when(studentMapper.toResponse(student)).thenReturn(expectedResponse);

        StudentDto.StudentResponse result = studentService.findById(studentId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(studentId);
        assertThat(result.fullName()).isEqualTo("John Doe");

        verify(studentRepository).findById(studentId);
    }

    @Test
    void findAll() {
        Student student2 = Student.builder()
                .id(UUID.randomUUID())
                .fullName("Jane Smith")
                .email("jane@example.com")
                .createAt(now)
                .updateAt(now)
                .build();

        List<Student> students = List.of(student, student2);

        when(studentRepository.findAll()).thenReturn(students);

        StudentDto.StudentResponse response1 = new StudentDto.StudentResponse(
                studentId, "john.doe@example.com", "John Doe", now, now
        );
        StudentDto.StudentResponse response2 = new StudentDto.StudentResponse(
                student2.getId(), "jane@example.com", "Jane Smith", now, now
        );

        when(studentMapper.toResponse(student)).thenReturn(response1);
        when(studentMapper.toResponse(student2)).thenReturn(response2);

        List<StudentDto.StudentResponse> result = studentService.findAll();

        assertThat(result).hasSize(2);

        verify(studentRepository).findAll();
    }

    @Test
    void delete() {
        when(studentRepository.existsById(studentId)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(studentId);

        studentService.delete(studentId);

        verify(studentRepository).existsById(studentId);
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void findByFullName() {
        List<Student> students = List.of(student);

        when(studentRepository.findByFullName("John Doe")).thenReturn(students);

        StudentDto.StudentResponse response = new StudentDto.StudentResponse(
                studentId, "john.doe@example.com", "John Doe", now, now
        );
        when(studentMapper.toResponse(student)).thenReturn(response);

        List<StudentDto.StudentResponse> result = studentService.findByFullName("John Doe");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).fullName()).isEqualTo("John Doe");

        verify(studentRepository).findByFullName("John Doe");
    }

    @Test
    void findByEmail() {
        List<Student> students = List.of(student);

        when(studentRepository.findByEmail("john.doe@example.com")).thenReturn(students);

        StudentDto.StudentResponse response = new StudentDto.StudentResponse(
                studentId, "john.doe@example.com", "John Doe", now, now
        );
        when(studentMapper.toResponse(student)).thenReturn(response);

        List<StudentDto.StudentResponse> result = studentService.findByEmail("john.doe@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo("john.doe@example.com");

        verify(studentRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void findStudentsCreatedAfter() {
        Instant pastDate = now.minus(10, ChronoUnit.DAYS);
        List<Student> students = List.of(student);

        when(studentRepository.findStudentsCreatedAfter(pastDate)).thenReturn(students);

        StudentDto.StudentResponse response = new StudentDto.StudentResponse(
                studentId, "john.doe@example.com", "John Doe", now, now
        );
        when(studentMapper.toResponse(student)).thenReturn(response);

        List<StudentDto.StudentResponse> result = studentService.findStudentsCreatedAfter(pastDate);

        assertThat(result).hasSize(1);

        verify(studentRepository).findStudentsCreatedAfter(pastDate);
    }

    @Test
    void findStudentsByNameLike() {
        List<Student> students = List.of(student);

        when(studentRepository.findStudentsByNameLike("John")).thenReturn(students);

        StudentDto.StudentResponse response = new StudentDto.StudentResponse(
                studentId, "john.doe@example.com", "John Doe", now, now
        );
        when(studentMapper.toResponse(student)).thenReturn(response);

        List<StudentDto.StudentResponse> result = studentService.findStudentsByNameLike("John");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).fullName()).contains("John");

        verify(studentRepository).findStudentsByNameLike("John");
    }
}