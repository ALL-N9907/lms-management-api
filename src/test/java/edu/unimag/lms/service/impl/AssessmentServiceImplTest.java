package edu.unimag.lms.service.impl;

import edu.unimag.lms.api.dto.AssessmentDto;
import edu.unimag.lms.domine.Entities.Assessment;
import edu.unimag.lms.domine.Entities.Course;
import edu.unimag.lms.domine.Entities.Student;
import edu.unimag.lms.domine.Repositories.AssessmentRepository;
import edu.unimag.lms.domine.Repositories.CourseRepository;
import edu.unimag.lms.domine.Repositories.InstructorRepository;
import edu.unimag.lms.domine.Repositories.StudentRepository;
import edu.unimag.lms.service.AssessmentService;
import edu.unimag.lms.service.mapper.AssessmentMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private AssessmentMapper assessmentMapper;

    private Assessment assessment;
    private Course course;
    private Student student;
    private Instant now;
    private UUID assessmentId;
    private UUID courseId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        assessmentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        now = Instant.now();

        course = Course.builder()
                .id(courseId)
                .title("Course Test")
                .status("ACTIVO")
                .active(true)
                .createAt(now)
                .updateAt(now)
                .build();

        student = Student.builder()
                .id(studentId)
                .fullName("Student Test")
                .email("student@test.com")
                .createAt(now)
                .updateAt(now)
                .build();

        assessment = Assessment.builder()
                .id(assessmentId)
                .type("QUIZ")
                .score(85)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();
    }

    @Test
    void create() {

        AssessmentDto.AssessmentCreateRequest createRequest =
                new AssessmentDto.AssessmentCreateRequest(
                        studentId,
                        courseId,
                        "EXAM",
                        9,
                        now
                );

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Assessment assessmentToSave = Assessment.builder()
                .type(createRequest.type())
                .score(createRequest.score())
                .course(course)
                .student(student)
                .takenAt(createRequest.takenAt())
                .build();

        when(assessmentMapper.toEntity(createRequest)).thenReturn(assessmentToSave);
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessmentToSave);

        AssessmentDto.AssesmentResponse expectedResponse = new AssessmentDto.AssesmentResponse(
                assessmentId, studentId, courseId, "EXAM", 9, now
        );
        when(assessmentMapper.toResponse(any(Assessment.class))).thenReturn(expectedResponse);

        AssessmentDto.AssesmentResponse result = assessmentService.create(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.score()).isEqualTo(9);
        assertThat(result.type()).isEqualTo("EXAM");

        verify(assessmentMapper, times(1)).toEntity(createRequest);
        verify(assessmentRepository, times(1)).save(any(Assessment.class));

    }

    @Test
    void update() {
        AssessmentDto.AssesmentUpdateRequest updateRequest = new AssessmentDto.AssesmentUpdateRequest(10);

        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        Assessment updatedAssessment = Assessment.builder()
                .id(assessmentId)
                .type(assessment.getType())
                .score(10)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        when(assessmentRepository.save(any(Assessment.class))).thenReturn(updatedAssessment);

        AssessmentDto.AssesmentResponse expectedResponse = new AssessmentDto.AssesmentResponse(
                assessmentId, studentId, courseId, assessment.getType(), 10, now
        );
        when(assessmentMapper.toResponse(updatedAssessment)).thenReturn(expectedResponse);

        AssessmentDto.AssesmentResponse result = assessmentService.update(assessmentId, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.score()).isEqualTo(10);

        verify(assessmentRepository, times(1)).findById(assessmentId);
        verify(assessmentRepository, times(1)).save(any(Assessment.class));
    }

    @Test
    void findById() {
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        AssessmentDto.AssesmentResponse expectedResponse = new AssessmentDto.AssesmentResponse(
                assessmentId, studentId, courseId, assessment.getType(), assessment.getScore(), now
        );
        when(assessmentMapper.toResponse(assessment)).thenReturn(expectedResponse);

        AssessmentDto.AssesmentResponse result = assessmentService.findById(assessmentId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(assessmentId);
        assertThat(result.score()).isEqualTo(8);

        verify(assessmentRepository, times(1)).findById(assessmentId);
        verify(assessmentMapper, times(1)).toResponse(assessment);
    }

    @Test
    void findAll() {
        Assessment assessment2 = Assessment.builder()
                .id(UUID.randomUUID())
                .type("HOMEWORK")
                .score(7)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        List<Assessment> assessments = List.of(assessment, assessment2);

        when(assessmentRepository.findAll()).thenReturn(assessments);

        AssessmentDto.AssesmentResponse response1 = new AssessmentDto.AssesmentResponse(
                assessmentId, studentId, courseId, "QUIZ", 8, now
        );
        AssessmentDto.AssesmentResponse response2 = new AssessmentDto.AssesmentResponse(
                assessment2.getId(), studentId, courseId, "HOMEWORK", 7, now
        );

        when(assessmentMapper.toResponse(assessment)).thenReturn(response1);
        when(assessmentMapper.toResponse(assessment2)).thenReturn(response2);

        List<AssessmentDto.AssesmentResponse> result = assessmentService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(AssessmentDto.AssesmentResponse::score)
                .containsExactlyInAnyOrder(8, 7);

        verify(assessmentRepository, times(1)).findAll();
    }

    @Test
    void delete() {
        when(assessmentRepository.existsById(assessmentId)).thenReturn(true);
        doNothing().when(assessmentRepository).deleteById(assessmentId);

        assessmentService.delete(assessmentId);

        verify(assessmentRepository).existsById(assessmentId);
        verify(assessmentRepository).deleteById(assessmentId);
    }

    @Test
    void findByScoreGreaterThan() {
        Assessment assessment8 = Assessment.builder()
                .id(UUID.randomUUID())
                .score(8)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        Assessment assessment9 = Assessment.builder()
                .id(UUID.randomUUID())
                .score(9)
                .course(course)
                .student(student)
                .takenAt(now)
                .build();

        List<Assessment> assessments = List.of(assessment8, assessment9);

        when(assessmentRepository.findByScoreGreaterThan(7)).thenReturn(assessments);

        AssessmentDto.AssesmentResponse response8 = new AssessmentDto.AssesmentResponse(
                assessment8.getId(), studentId, courseId, "QUIZ", 8, now
        );
        AssessmentDto.AssesmentResponse response9 = new AssessmentDto.AssesmentResponse(
                assessment9.getId(), studentId, courseId, "EXAM", 9, now
        );

        when(assessmentMapper.toResponse(assessment8)).thenReturn(response8);
        when(assessmentMapper.toResponse(assessment9)).thenReturn(response9);

        List<AssessmentDto.AssesmentResponse> result = assessmentService.findByScoreGreaterThan(7);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(AssessmentDto.AssesmentResponse::score)
                .containsExactlyInAnyOrder(8, 9);

        verify(assessmentRepository, times(1)).findByScoreGreaterThan(7);
    }

    @Test
    void findByTakenAtAfter() {
        Instant futureDate = now.plus(5, ChronoUnit.DAYS);

        Assessment assessmentFuture = Assessment.builder()
                .id(UUID.randomUUID())
                .score(9)
                .course(course)
                .student(student)
                .takenAt(futureDate)
                .build();

        List<Assessment> assessments = List.of(assessmentFuture);

        when(assessmentRepository.findByTakenAtAfter(now)).thenReturn(assessments);

        AssessmentDto.AssesmentResponse expectedResponse = new AssessmentDto.AssesmentResponse(
                assessmentFuture.getId(), studentId, courseId, "EXAM", 9, futureDate
        );
        when(assessmentMapper.toResponse(assessmentFuture)).thenReturn(expectedResponse);

        List<AssessmentDto.AssesmentResponse> result = assessmentService.findByTakenAtAfter(now);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).score()).isEqualTo(9);

        verify(assessmentRepository, times(1)).findByTakenAtAfter(now);
    }

    @Test
    void findAssessmentScore() {
        List<Assessment> assessments = List.of(assessment);

        when(assessmentRepository.findAssessmentScore(8)).thenReturn(assessments);

        AssessmentDto.AssesmentResponse expectedResponse = new AssessmentDto.AssesmentResponse(
                assessmentId, studentId, courseId, "QUIZ", 8, now
        );
        when(assessmentMapper.toResponse(assessment)).thenReturn(expectedResponse);

        List<AssessmentDto.AssesmentResponse> result = assessmentService.findAssessmentScore(8);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).score()).isEqualTo(8);

        verify(assessmentRepository, times(1)).findAssessmentScore(8);
    }

    @Test
    void findAssessmentTakenAt() {
        List<Assessment> assessments = List.of(assessment);

        when(assessmentRepository.findAssessmentTakenAt(now)).thenReturn(assessments);

        AssessmentDto.AssesmentResponse expectedResponse = new AssessmentDto.AssesmentResponse(
                assessmentId, studentId, courseId, "QUIZ", 8, now
        );
        when(assessmentMapper.toResponse(assessment)).thenReturn(expectedResponse);

        List<AssessmentDto.AssesmentResponse> result = assessmentService.findAssessmentTakenAt(now);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).takenAt()).isEqualTo(now);

        verify(assessmentRepository, times(1)).findAssessmentTakenAt(now);
    }
}