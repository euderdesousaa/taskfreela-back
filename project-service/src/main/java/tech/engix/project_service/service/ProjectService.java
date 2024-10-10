package tech.engix.project_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.engix.project_service.dto.ProjectRequest;
import tech.engix.project_service.mapper.ProjectMapper;
import tech.engix.project_service.model.Project;
import tech.engix.project_service.repository.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    public List<Project> listAll(){
        return repository.findAll();
    }

    public ProjectRequest createProject(ProjectRequest dto) {
        Project project = mapper.toEntity(dto);
        return mapper.toProjectRequest(repository.save(project));
    }
}
