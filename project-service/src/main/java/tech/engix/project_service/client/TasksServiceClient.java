package tech.engix.project_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${tech.engix.service.url}", name = "tasks-service")
public interface TasksServiceClient {

    @GetMapping("api/v1/tasks/count/in-progress")
    Long countInProgressTasksByProjectId(@RequestParam("projectId") Long projectId);

    @GetMapping("api/v1/tasks/count/completed")
    Long countCompletedTasksByProjectId(@RequestParam("projectId") Long projectId);
}
