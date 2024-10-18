package tech.engix.project_service.mapper;

import org.mapstruct.Mapper;
import tech.engix.project_service.dto.ProjectRequest;
import tech.engix.project_service.dto.ProjectResponse;
import tech.engix.project_service.dto.ProjectUpdateRequest;
import tech.engix.project_service.model.Project;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toEntity(ProjectRequest projectRequest);
    ProjectRequest toProjectRequest(Project project);

    ProjectUpdateRequest toUpdate(Project save);
    Project toUpdateEntity(ProjectUpdateRequest project);

    List<ProjectResponse> toProjectResponse(List<Project> projects);
}
