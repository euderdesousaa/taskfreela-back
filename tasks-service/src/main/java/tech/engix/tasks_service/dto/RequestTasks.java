package tech.engix.tasks_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import tech.engix.tasks_service.model.enums.Label;
import tech.engix.tasks_service.model.enums.Priority;
import tech.engix.tasks_service.model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public record RequestTasks(
        String title,
        @Enumerated(EnumType.STRING)
        Status status,
        @Enumerated(EnumType.STRING)
        Priority priority,
        @Enumerated(EnumType.STRING)
        Label label,
        List<String> columns,
        @JsonFormat(pattern = "dd/MM/yyyy:hh:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy:hh:mm:ss")
        LocalDateTime updateTime
) {
}
