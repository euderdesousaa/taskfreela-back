package tech.engix.project_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.engix.jwtutils.service.JwtUtils;
import tech.engix.project_service.client.UserServiceClient;
import tech.engix.project_service.dto.ProjectRequest;
import tech.engix.project_service.dto.ProjectResponse;
import tech.engix.project_service.dto.ProjectUpdateRequest;
import tech.engix.project_service.dto.UserResponse;
import tech.engix.project_service.mapper.ProjectMapper;
import tech.engix.project_service.model.Project;
import tech.engix.project_service.repository.ProjectRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final JwtUtils jwtUtils;
    private final UserServiceClient userServiceClient;

    public List<ProjectResponse> listAll(String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

        UserResponse user = userServiceClient.getUserByEmail(email);

        if (user != null) {
            return mapper.toProjectResponse(repository.findByUserId(user.id()));
        }

        return Collections.emptyList();
    }

    public ProjectRequest createProject(ProjectRequest dto, String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse user = userServiceClient.getUserByEmail(email);

        Project project = mapper.toEntity(dto);

        project.setUserId(user.id());
        return mapper.toProjectRequest(repository.save(project));
    }

    public ProjectUpdateRequest editProject(ProjectUpdateRequest update, Long id, String jwtToken) {

        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse user = userServiceClient.getUserByEmail(email);

        Optional<Project> projectOptional = repository.findById(id);

        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();

            if (project.getUserId().equals(user.id())) {
                Project toMapper = mapper.toUpdateEntity(update);
                return mapper.toUpdate(repository.save(toMapper));
            } else {
                throw new RuntimeException("Unauthorized: You do not have permission to edit this tasks.");
            }
        } else {
            throw new RuntimeException("Client not found with id " + id);
        }
    }

    public void deleteProject(Long id, String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse user = userServiceClient.getUserByEmail(email);

        if (repository.existsByIdAndUserId(id, user.id())) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Client not found or user not authorized to delete this client.");
        }
    }


}
