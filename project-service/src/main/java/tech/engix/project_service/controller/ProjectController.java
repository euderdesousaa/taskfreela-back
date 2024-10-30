package tech.engix.project_service.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.engix.project_service.dto.*;
import tech.engix.project_service.service.ProjectService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService service;

    @GetMapping("/details/{id}")
    public ResponseEntity<ProjectResponse> listProjectDetails(@PathVariable Long id,
            HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(service.getProjectDetails(id, jwtToken));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ProjectSummaryResponse>> listProjectSummary(
            HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(service.listSummary(jwtToken));
    }

    @GetMapping("/getProjectId")
    @Hidden
    public ResponseEntity<Optional<ProjectClientResponse>> getClientByName(@RequestParam("id") Long id){
        Optional<ProjectClientResponse> project = service.getProjectId(id);
        if (project.isPresent()) {
            return ResponseEntity.ok(project);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create/{idClient}")
    public ResponseEntity<ProjectCreateResponse> createTask(@RequestBody ProjectRequest requestProject,
                                                     @PathVariable Long idClient,
                                                     HttpServletRequest request) {

        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectCreateResponse project = service.createProject(requestProject, jwtToken, idClient);

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
