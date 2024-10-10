package tech.engix.project_service.mapper;

import org.mapstruct.Mapper;
import tech.engix.project_service.dto.ProjectRequest;
import tech.engix.project_service.model.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toEntity(ProjectRequest projectRequest);
    ProjectRequest toProjectRequest(Project project);
}
