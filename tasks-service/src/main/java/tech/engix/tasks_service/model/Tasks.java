package tech.engix.tasks_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.engix.tasks_service.model.enums.Label;
import tech.engix.tasks_service.model.enums.Priority;
import tech.engix.tasks_service.model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")
public class Tasks {

    private String id;

    private String title;

    private Long projectId;

    private String projectName;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Label label;

    private List<String> columns;

    @JsonFormat(pattern = "dd/MM/yyyy:hh:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy:hh:mm:ss")
    private LocalDateTime updateTime;
}
