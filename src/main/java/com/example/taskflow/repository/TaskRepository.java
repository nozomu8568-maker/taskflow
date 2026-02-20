package com.example.taskflow.repository;

import com.example.taskflow.domain.entity.Task;
import com.example.taskflow.user.User; // ★追加
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional; // ★追加

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
  Optional<Task> findByIdAndOwner(Long id, User owner); // ★追加
}