package tech.engix.project_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record ProjectRequest(
        String name,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date createTime,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date finishTime,
        Double budget,
        String team
) {
}
