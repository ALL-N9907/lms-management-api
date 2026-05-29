package edu.unimag.lms.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class StudentDto {

    public record StudentCreateRequest(
            @Email @NotBlank String email,
            @NotBlank String fullName
    ) implements Serializable{}

    public record StudentUpdateRequest(
            String email
    ) implements Serializable{}

    public record StudentResponse(
            UUID id,
            String email,
            String fullName,
            Instant createdAt,
            Instant updatedAt
    ) implements Serializable{}
}
