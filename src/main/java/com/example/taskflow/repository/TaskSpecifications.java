package com.example.taskflow.repository;

import com.example.taskflow.domain.entity.Task;
import com.example.taskflow.domain.enums.Priority;
import com.example.taskflow.domain.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecifications {

  public static Specification<Task> titleOrDescriptionContains(String q) {
    return (root, query, cb) -> {
      if (q == null || q.isBlank()) return cb.conjunction();
      String like = "%" + q.trim().toLowerCase() + "%";
      return cb.or(
          cb.like(cb.lower(root.get("title")), like),
          cb.like(cb.lower(root.get("description")), like)
      );
    };
  }

  public static Specification<Task> hasStatus(Status status) {
    return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
  }

  public static Specification<Task> hasPriority(Priority priority) {
    return (root, query, cb) -> priority == null ? cb.conjunction() : cb.equal(root.get("priority"), priority);
  }

  public static Specification<Task> dueDateFrom(LocalDate from) {
    return (root, query, cb) -> from == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("dueDate"), from);
  }

  public static Specification<Task> dueDateTo(LocalDate to) {
    return (root, query, cb) -> to == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("dueDate"), to);
  }
}
