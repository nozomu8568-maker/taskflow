package com.example.taskflow.service;

import com.example.taskflow.domain.entity.Task;
import com.example.taskflow.domain.enums.Priority;
import com.example.taskflow.domain.enums.Status;
import com.example.taskflow.dto.TaskCreateRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.dto.TaskUpdateRequest;
import com.example.taskflow.exception.NotFoundException;
import com.example.taskflow.repository.TaskRepository;
import com.example.taskflow.repository.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

  private final TaskRepository taskRepository;

  // =============================
  // Create
  // =============================
  public TaskResponse create(TaskCreateRequest req) {
    Task saved = taskRepository.save(Task.builder()
        .title(req.title())
        .description(req.description())
        .status(req.status())
        .priority(req.priority())
        .dueDate(req.dueDate())
        .build());
    return toResponse(saved);
  }

  // =============================
  // Search + Pageable
  // =============================
  @Transactional(readOnly = true)
  public Page<TaskResponse> search(
      String q,
      Status status,
      Priority priority,
      LocalDate dueFrom,
      LocalDate dueTo,
      Pageable pageable
  ) {

    Specification<Task> spec =
        TaskSpecifications.titleOrDescriptionContains(q)
            .and(TaskSpecifications.hasStatus(status))
            .and(TaskSpecifications.hasPriority(priority))
            .and(TaskSpecifications.dueDateFrom(dueFrom))
            .and(TaskSpecifications.dueDateTo(dueTo));

    return taskRepository.findAll(spec, pageable)
        .map(this::toResponse);
  }

  // =============================
  // Get by ID
  // =============================
  @Transactional(readOnly = true)
  public TaskResponse get(Long id) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Task not found: id=" + id));
    return toResponse(task);
  }

  // =============================
  // Update
  // =============================
  public TaskResponse update(Long id, TaskUpdateRequest req) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Task not found: id=" + id));

    task.setTitle(req.title());
    task.setDescription(req.description());
    task.setStatus(req.status());
    task.setPriority(req.priority());
    task.setDueDate(req.dueDate());

    return toResponse(task);
  }

  // =============================
  // Delete
  // =============================
  public void delete(Long id) {
    if (!taskRepository.existsById(id)) {
      throw new NotFoundException("Task not found: id=" + id);
    }
    taskRepository.deleteById(id);
  }

  // =============================
  // Toggle Complete
  // =============================
  public TaskResponse toggleComplete(Long id) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Task not found: id=" + id));

    task.setStatus(task.getStatus() == Status.DONE ? Status.TODO : Status.DONE);
    return toResponse(task);
  }

  // =============================
  // Mapper
  // =============================
  private TaskResponse toResponse(Task t) {
    return new TaskResponse(
        t.getId(),
        t.getTitle(),
        t.getDescription(),
        t.getStatus(),
        t.getPriority(),
        t.getDueDate()
    );
  }
}
