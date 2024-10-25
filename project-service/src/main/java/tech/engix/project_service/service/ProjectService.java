package tech.engix.project_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.engix.jwtutils.service.JwtUtilsService;
import tech.engix.project_service.client.ClientServiceClient;
import tech.engix.project_service.client.UserServiceClient;
import tech.engix.project_service.dto.*;
import tech.engix.project_service.mapper.ProjectMapper;
import tech.engix.project_service.model.Project;
import tech.engix.project_service.repository.ProjectRepository;
import tech.engix.project_service.service.exception.exceptions.AccessDeniedException;
import tech.engix.project_service.service.exception.exceptions.ClientNotFound;
import tech.engix.project_service.service.exception.exceptions.ProjectNotFoundException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final JwtUtilsService jwtUtils;
    private final UserServiceClient userServiceClient;
    private final ClientServiceClient clientServiceClient;

    public List<ProjectResponse> listAll(String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

        UserResponse user = userServiceClient.getUserByEmail(email);

        if (user != null) {
            List<Project> projects = repository.findByUserId(user.id());

            return projects.stream()
                    .sorted(Comparator.comparing(Project::getId))
                    .map(project -> {
                        ClientResponse client = clientServiceClient.getClientName(project.getClientId());

                        ProjectResponse projectResponse = mapper.toProjectResponse(project);

                        projectResponse.setClientName(client.name());

                        return projectResponse;
                    })
                    .toList();
        }

        return Collections.emptyList();
    }

    public Optional<ProjectClientResponse> getProjectId(Long id) {
        Project findById = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return Optional.ofNullable(mapper.toProjectClientResponse(findById));
    }

    @Transactional
    public ProjectRequest createProject(ProjectRequest dto, String jwtToken, Long id) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse user = userServiceClient.getUserByEmail(email);
        ClientResponse client = clientServiceClient.getClientName(id);

        Project project = mapper.toEntity(dto);

        project.setUserId(user.id());
        project.setClientId(client.id());
        return mapper.toProjectRequest(repository.save(project));
    }

    @Transactional
    public ProjectUpdateRequest editProject(ProjectUpdateRequest update, Long id, String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse user = userServiceClient.getUserByEmail(email);

        Optional<Project> projectOptional = repository.findById(id);

        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();

            if (project.getUserId().equals(user.id())) {
                mapper.toUpdateEntity(update, project);

                return mapper.toUpdate(repository.save(project));
            } else {
                throw new AccessDeniedException("Unauthorized: You do not have permission to edit this project.");
            }
        } else {
            throw new ProjectNotFoundException("Project not found with id " + id);
        }
    }

    @Transactional
    public void deleteProject(Long id, String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse user = userServiceClient.getUserByEmail(email);

        if (repository.existsByIdAndUserId(id, user.id())) {
            repository.deleteById(id);
        } else {
            throw new ClientNotFound("Client not found or user not authorized to delete this client.");
        }
    }


}
