package tech.engix.tasks_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.engix.jwtutils.service.JwtUtils;
import tech.engix.tasks_service.client.UserServiceClient;
import tech.engix.tasks_service.dto.RequestTasks;
import tech.engix.tasks_service.dto.TasksUpdateRequest;
import tech.engix.tasks_service.dto.UserResponse;
import tech.engix.tasks_service.mapper.TasksMapper;
import tech.engix.tasks_service.model.Tasks;
import tech.engix.tasks_service.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TasksService {

    private final TasksMapper mapper;

    private final TaskRepository repository;

    private final JwtUtils jwtUtils;

    private final UserServiceClient userServiceClient;

    public List<Tasks> listAll(String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

        UserResponse user = userServiceClient.getUserByEmail(email);

        if (user != null) {
            return repository.findByUserId(user.id());
        }

        return Collections.emptyList();
    }


    public RequestTasks createTasks(RequestTasks requestTasks, String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse user = userServiceClient.getUserByEmail(email);

        Tasks tasks = mapper.toDto(requestTasks);
        tasks.setUserId(user.id());
        tasks.setCreatedAt(LocalDateTime.now());
        repository.save(tasks);

        return mapper.toEntity(tasks);
    }

    public TasksUpdateRequest editTasks(RequestTasks update, String id, String jwtToken) {

        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse user = userServiceClient.getUserByEmail(email);

        Optional<Tasks> optionalClient = repository.findById(id);

        if (optionalClient.isPresent()) {
            Tasks tasks = optionalClient.get();

            if (tasks.getUserId().equals(user.id())) {
                tasks.setTitle(update.title());
                Tasks updatedClient = repository.save(tasks);
                tasks.setCreatedAt(LocalDateTime.now());
                return mapper.toUpdate(updatedClient);
            } else {
                throw new RuntimeException("Unauthorized: You do not have permission to edit this tasks.");
            }
        } else {
            throw new RuntimeException("Client not found with id " + id);
        }
    }

    public void deleteTasks(String id) {
        repository.deleteById(id);
    }
}
