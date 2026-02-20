package com.example.taskflow.domain.entity;

import com.example.taskflow.domain.enums.Priority;
import com.example.taskflow.domain.enums.Status;
import com.example.taskflow.user.User;          // ★追加
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(length = 1000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Priority priority;

  private LocalDate dueDate;

  // ★追加：タスク所有者
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;
}