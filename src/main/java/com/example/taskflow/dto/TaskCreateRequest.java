package com.example.taskflow.dto;

import com.example.taskflow.domain.enums.Priority;
import com.example.taskflow.domain.enums.Status;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record TaskCreateRequest(
    @NotBlank @Size(max = 100) String title,
    @Size(max = 1000) String description,
    @NotNull Status status,
    @NotNull Priority priority,
    LocalDate dueDate
) {}
