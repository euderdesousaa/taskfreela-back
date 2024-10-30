package tech.engix.project_service.mapper;

import org.mapstruct.*;
import tech.engix.project_service.dto.*;
import tech.engix.project_service.model.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toEntity(ProjectRequest projectRequest);
    ProjectRequest toProjectRequest(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProjectUpdateRequest toUpdate(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "id", ignore = true)
    void toUpdateEntity(ProjectUpdateRequest projectUpdateRequest, @MappingTarget Project project);

    @Mapping(target = "clientName", ignore = true)
    ProjectResponse toProjectResponse(Project project);

    ProjectClientResponse toProjectClientResponse(Project project);

    ProjectCreateResponse toClientResp(Project project);

    ProjectSummaryResponse toSummaryResponse(Project project);
}
