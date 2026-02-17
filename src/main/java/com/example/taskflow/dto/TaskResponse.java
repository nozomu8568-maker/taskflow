package com.example.taskflow.dto;

import com.example.taskflow.domain.enums.Priority;
import com.example.taskflow.domain.enums.Status;

import java.time.LocalDate;

public record TaskResponse(
    Long id,
    String title,
    String description,
    Status status,
    Priority priority,
    LocalDate dueDate
) {}
