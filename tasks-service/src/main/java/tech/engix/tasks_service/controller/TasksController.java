package tech.engix.tasks_service.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.engix.tasks_service.dto.RequestTasks;
import tech.engix.tasks_service.dto.TasksUpdateRequest;
import tech.engix.tasks_service.model.Tasks;
import tech.engix.tasks_service.service.TasksService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TasksController {

    private final TasksService service;

    @GetMapping()
    public ResponseEntity<List<Tasks>> listAllTasks(
            HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(service.listAll( jwtToken));
    }

    @PostMapping("/createTask")
    public ResponseEntity<RequestTasks> createTask(@RequestBody RequestTasks requestTasks, HttpServletRequest request) {

        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        RequestTasks tasks = service.createTasks(requestTasks, jwtToken);

        return ResponseEntity.ok().body(tasks);
    }

    @PutMapping("/editTask/{id}")
    public ResponseEntity<TasksUpdateRequest> editTask(@PathVariable String id, @RequestBody RequestTasks requestTasks, HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);
        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TasksUpdateRequest tasks = service.editTasks(requestTasks, jwtToken, id);

        return ResponseEntity.ok().body(tasks);
    }

    @DeleteMapping("deleteTask/{id}")
    public void deleteTask(@PathVariable("id") String id) {
        service.deleteTasks(id);
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
