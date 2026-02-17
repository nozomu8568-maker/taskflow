package com.example.taskflow.controller;

import com.example.taskflow.domain.enums.Priority;
import com.example.taskflow.domain.enums.Status;
import com.example.taskflow.dto.PageResponse;
import com.example.taskflow.dto.TaskCreateRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.dto.TaskUpdateRequest;
import com.example.taskflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  // =============================
  // Create
  // =============================
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskResponse create(@Valid @RequestBody TaskCreateRequest req) {
    return taskService.create(req);
  }

  // =============================
  // Search + Pageable (stable response)
  // =============================
  @GetMapping
  public PageResponse<TaskResponse> list(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Status status,
      @RequestParam(required = false) Priority priority,
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate dueFrom,
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate dueTo,
      Pageable pageable
  ) {
    return PageResponse.from(
        taskService.search(q, status, priority, dueFrom, dueTo, pageable)
    );
  }

  // =============================
  // Get by ID
  // =============================
  @GetMapping("/{id}")
  public TaskResponse get(@PathVariable Long id) {
    return taskService.get(id);
  }

  // =============================
  // Update
  // =============================
  @PutMapping("/{id}")
  public TaskResponse update(
      @PathVariable Long id,
      @Valid @RequestBody TaskUpdateRequest req
  ) {
    return taskService.update(id, req);
  }

  // =============================
  // Delete
  // =============================
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    taskService.delete(id);
  }

  // =============================
  // Toggle Complete
  // =============================
  @PatchMapping("/{id}/complete")
  public TaskResponse toggleComplete(@PathVariable Long id) {
    return taskService.toggleComplete(id);
  }
}
