package tech.engix.project_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectSummaryResponse {
    private Long id;

    private String name;

    private String clientName;

    private Long qntTasks;
    private Long completedTasks;
}
