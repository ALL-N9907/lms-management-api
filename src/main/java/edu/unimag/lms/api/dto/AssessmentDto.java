package edu.unimag.lms.api.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class AssessmentDto {

    public record AssessmentCreateRequest(
            @NotNull UUID studentId,
            @NotNull UUID courseId,
            @NotBlank String type,
            @Min(0) @Max(10) int score,
            @NotNull Instant takenAt
    ) implements Serializable{}

    public record AssesmentUpdateRequest(
            int score
    ) implements Serializable{}

    public record AssesmentResponse(
            UUID id,
            UUID studentId,
            UUID courseId,
            String type,
            int score,
            Instant takenAt
    ) implements Serializable{}
}
