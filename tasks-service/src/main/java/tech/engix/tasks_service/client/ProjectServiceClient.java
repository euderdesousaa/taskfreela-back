package tech.engix.tasks_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.engix.tasks_service.dto.ProjectClientResponse;

@FeignClient(url = "${tech.engix.service.url}", name = "project-service")
public interface ProjectServiceClient {
    @GetMapping("api/v1/project/getProjectId")
    ProjectClientResponse getClientName(@RequestParam("id") Long id);
}
