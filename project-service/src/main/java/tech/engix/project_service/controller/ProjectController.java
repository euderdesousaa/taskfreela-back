package tech.engix.project_service.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.engix.project_service.dto.ProjectRequest;
import tech.engix.project_service.dto.ProjectResponse;
import tech.engix.project_service.dto.ProjectUpdateRequest;
import tech.engix.project_service.service.ProjectService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService service;

    @GetMapping()
    public ResponseEntity<List<ProjectResponse>> listAllProject(
            HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(service.listAll(jwtToken));
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectRequest> createTask(@RequestBody ProjectRequest requestProject,
                                                     HttpServletRequest request) {

        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectRequest project = service.createProject(requestProject, jwtToken);

        return ResponseEntity.ok().body(project);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProjectUpdateRequest> editProject(@PathVariable Long id,
                                                         @RequestBody ProjectUpdateRequest requestProject,
                                                         HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);
        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectUpdateRequest project = service.editProject(requestProject, id, jwtToken);

        return ResponseEntity.ok().body(project);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTask(@PathVariable("id") Long id,
                           HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);
        service.deleteProject(id, jwtToken);
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;

    }
}
