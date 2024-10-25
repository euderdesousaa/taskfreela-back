package tech.engix.project_service.mapper;

import org.mapstruct.*;
import tech.engix.project_service.dto.ProjectClientResponse;
import tech.engix.project_service.dto.ProjectRequest;
import tech.engix.project_service.dto.ProjectResponse;
import tech.engix.project_service.dto.ProjectUpdateRequest;
import tech.engix.project_service.model.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toEntity(ProjectRequest projectRequest);
    ProjectRequest toProjectRequest(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProjectUpdateRequest toUpdate(Project project); // Corrigido para aceitar o projeto atual

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "clientId", ignore = true) // Ignorando campos desnecessários
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "id", ignore = true)
    void toUpdateEntity(ProjectUpdateRequest projectUpdateRequest, @MappingTarget Project project); // Agora aceitando um objeto existente

    @Mapping(target = "clientName", ignore = true) // Corrigido para o mapeamento de resposta
    ProjectResponse toProjectResponse(Project project);

    ProjectClientResponse toProjectClientResponse(Project project);

}
