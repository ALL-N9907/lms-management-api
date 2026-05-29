package edu.unimag.lms.api.dto;

import edu.unimag.lms.domine.Entities.Course;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class InstructorDto {

    public record InstructorCreateRequest(
            @Email @NotBlank String email,
            @NotBlank String fullName
    ) implements Serializable {}

    public record InstructorUpdateRequest(
            String email
    ) implements Serializable {}

    public record InstructorResponse(
            UUID id,
            String email,
            String fullName,
            Instant createdAt,
            Instant updateAt

    ) implements Serializable {}
}
