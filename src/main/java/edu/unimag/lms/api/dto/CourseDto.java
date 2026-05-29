package edu.unimag.lms.api.dto;

import edu.unimag.lms.domine.Entities.Lesson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CourseDto {

    public record CourseCreateRequest(
            @NotBlank String title,
            @NotBlank String status,
            @NotNull UUID instructorId,
            boolean active

    ) implements Serializable{}

    public record CourseUpdateRequest(
            @NotBlank String title,
            @NotBlank String status,
            boolean active,
            @NotNull UUID instructorId

    ) implements Serializable{}

    public record CourseResponse(
            UUID id,
            UUID instructorId,
            String title,
            String Status,
            boolean active,
            int Enrollments,
            List<Lesson> lessons,
            Instant createdAt

    ) implements Serializable{}
}
