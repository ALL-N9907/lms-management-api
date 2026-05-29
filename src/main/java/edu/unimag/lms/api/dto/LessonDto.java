package edu.unimag.lms.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class LessonDto {

    public record LessonCreateRequest(
            @NotNull UUID courseId,
            @NotBlank String title,
            int orderIndex
    ) implements Serializable{}

    public record LessonUpdateRequest(
            String title,
            int orderIndex,
            UUID courseId
    ) implements Serializable{}

    public record LessonResponse(
            UUID id,
            String title,
            int orderIndex,
            UUID courseId
    ) implements Serializable{}
}
