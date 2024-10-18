package tech.engix.tasks_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private Long userId;

    private List<String> columns;
    @JsonFormat(pattern = "dd/MM/yyyy:hh:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy:hh:mm:ss")
    private LocalDateTime updateTime;
}
