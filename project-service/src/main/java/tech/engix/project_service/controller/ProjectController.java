package tech.engix.project_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.engix.project_service.dto.ProjectRequest;
import tech.engix.project_service.model.Project;
import tech.engix.project_service.service.ProjectService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService service;

    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return service.listAll();
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectRequest> createProject(@RequestBody ProjectRequest dto) {
        service.createProject(dto);
        return ResponseEntity.ok().body(dto);
    }
}
