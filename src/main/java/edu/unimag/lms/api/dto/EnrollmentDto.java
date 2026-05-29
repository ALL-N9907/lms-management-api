package edu.unimag.lms.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class EnrollmentDto {

    public record EnrollmentCreateRequest(
            @NotNull UUID studentId,
            @NotNull UUID courseId,
            @NotBlank String status
            ) implements Serializable{}

    public record EnrollmentUpdateRequest(
            @NotBlank String status
            ) implements Serializable{}

    public record EnrollmentResponse(
            UUID Id,
            UUID studentId,
            UUID courseId,
            String status,
            Instant enrolledAt
            ) implements Serializable{}
}
