package tech.engix.project_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProjectResponse {
    private Long id;

    private String name;

    private String clientName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createTime;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date finishTime;

    private Double budget;

    private String team;

}
