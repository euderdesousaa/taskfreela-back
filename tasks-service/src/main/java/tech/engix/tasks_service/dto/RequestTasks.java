package tech.engix.tasks_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record RequestTasks(
        String title,
        List<String> columns,
        @JsonFormat(pattern = "dd/MM/yyyy:hh:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy:hh:mm:ss")
        LocalDateTime updateTime
) {
}
