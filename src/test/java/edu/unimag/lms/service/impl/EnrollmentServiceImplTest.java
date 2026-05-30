package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.EnrollmentDto;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Enrollment;
import edu.unimag.lms.domine.Entities.Student;
import edu.unimag.lms.domine.Repositories.CourseRepository;
import edu.unimag.lms.domine.Repositories.EnrollmentRepository;
import edu.unimag.lms.domine.Repositories.StudentRepository;
import edu.unimag.lms.service.mapper.EnrollmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    private Enrollment enrollment;
    private Student student;
    private Course course;
    private Instant now;
    private UUID enrollmentId;
    private UUID studentId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        enrollmentId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        now = Instant.now();

        student = Student.builder()
                .id(studentId)
                .fullName("Student Test")
                .email("student@test.com")
                .createAt(now)
                .updateAt(now)
                .build();

        course = Course.builder()
                .id(courseId)
                .title("Course Test")
                .status("ACTIVO")
                .active(true)
                .createAt(now)
                .updateAt(now)
                .build();

        enrollment = Enrollment.builder()
                .id(enrollmentId)
                .student(student)
                .course(course)
                .status("ACTIVE")
                .enrolledAt(now)
                .build();
    }

    @Test
    void create() {
        EnrollmentDto.EnrollmentCreateRequest createRequest = new EnrollmentDto.EnrollmentCreateRequest(
                studentId, courseId, "ACTIVE"
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentMapper.toEntity(createRequest)).thenReturn(enrollment);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        EnrollmentDto.EnrollmentResponse expectedResponse = new EnrollmentDto.EnrollmentResponse(
                enrollmentId, studentId, courseId, "ACTIVE", now
        );
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(expectedResponse);

        EnrollmentDto.EnrollmentResponse result = enrollmentService.create(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.Id()).isEqualTo(enrollmentId);

        verify(studentRepository).findById(studentId);
        verify(courseRepository).findById(courseId);
        verify(enrollmentMapper).toEntity(createRequest);
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void update() {
        EnrollmentDto.EnrollmentUpdateRequest updateRequest = new EnrollmentDto.EnrollmentUpdateRequest("CANCELED");

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));

        Enrollment updatedEnrollment = Enrollment.builder()
                .id(enrollmentId)
                .student(student)
                .course(course)
                .status("CANCELED")
                .enrolledAt(now)
                .build();

        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(updatedEnrollment);

        EnrollmentDto.EnrollmentResponse expectedResponse = new EnrollmentDto.EnrollmentResponse(
                enrollmentId, studentId, courseId, "CANCELED", now
        );
        when(enrollmentMapper.toResponse(updatedEnrollment)).thenReturn(expectedResponse);

        EnrollmentDto.EnrollmentResponse result = enrollmentService.update(enrollmentId, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo("CANCELED");

        verify(enrollmentRepository).findById(enrollmentId);
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void findById() {
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));

        EnrollmentDto.EnrollmentResponse expectedResponse = new EnrollmentDto.EnrollmentResponse(
                enrollmentId, studentId, courseId, "ACTIVE", now
        );
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(expectedResponse);

        EnrollmentDto.EnrollmentResponse result = enrollmentService.findById(enrollmentId);

        assertThat(result).isNotNull();
        assertThat(result.Id()).isEqualTo(enrollmentId);

        verify(enrollmentRepository).findById(enrollmentId);
    }

    @Test
    void findAll() {
        Enrollment enrollment2 = Enrollment.builder()
                .id(UUID.randomUUID())
                .student(student)
                .course(course)
                .status("ACTIVE")
                .enrolledAt(now)
                .build();

        List<Enrollment> enrollments = List.of(enrollment, enrollment2);

        when(enrollmentRepository.findAll()).thenReturn(enrollments);

        EnrollmentDto.EnrollmentResponse response1 = new EnrollmentDto.EnrollmentResponse(
                enrollmentId, studentId, courseId, "ACTIVE", now
        );
        EnrollmentDto.EnrollmentResponse response2 = new EnrollmentDto.EnrollmentResponse(
                enrollment2.getId(), studentId, courseId, "ACTIVE", now
        );

        when(enrollmentMapper.toResponse(enrollment)).thenReturn(response1);
        when(enrollmentMapper.toResponse(enrollment2)).thenReturn(response2);

        List<EnrollmentDto.EnrollmentResponse> result = enrollmentService.findAll();

        assertThat(result).hasSize(2);

        verify(enrollmentRepository).findAll();
    }

    @Test
    void delete() {
        when(enrollmentRepository.existsById(enrollmentId)).thenReturn(true);
        doNothing().when(enrollmentRepository).deleteById(enrollmentId);

        enrollmentService.delete(enrollmentId);

        verify(enrollmentRepository).existsById(enrollmentId);
        verify(enrollmentRepository).deleteById(enrollmentId);
    }

    @Test
    void existsByStudentId() {
        when(enrollmentRepository.existsByStudentId(studentId)).thenReturn(true);

        boolean result = enrollmentService.existsByStudentId(studentId);

        assertThat(result).isTrue();

        verify(enrollmentRepository).existsByStudentId(studentId);
    }

    @Test
    void findByStatus() {
        List<Enrollment> enrollments = List.of(enrollment);

        when(enrollmentRepository.findByStatus("ACTIVE")).thenReturn(enrollments);

        EnrollmentDto.EnrollmentResponse response = new EnrollmentDto.EnrollmentResponse(
                enrollmentId, studentId, courseId, "ACTIVE", now
        );
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(response);

        List<EnrollmentDto.EnrollmentResponse> result = enrollmentService.findByStatus("ACTIVE");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo("ACTIVE");

        verify(enrollmentRepository).findByStatus("ACTIVE");
    }

    @Test
    void findEnrollmentEnrolledAt() {
        List<Enrollment> enrollments = List.of(enrollment);

        when(enrollmentRepository.findEnrollmentEnrolledAt(now)).thenReturn(enrollments);

        EnrollmentDto.EnrollmentResponse response = new EnrollmentDto.EnrollmentResponse(
                enrollmentId, studentId, courseId, "ACTIVE", now
        );
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(response);

        List<EnrollmentDto.EnrollmentResponse> result = enrollmentService.findEnrollmentEnrolledAt(now);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).enrolledAt()).isEqualTo(now);

        verify(enrollmentRepository).findEnrollmentEnrolledAt(now);
    }
}