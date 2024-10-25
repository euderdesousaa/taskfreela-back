package tech.engix.tasks_service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.engix.tasks_service.dto.RequestTasks;
import tech.engix.tasks_service.dto.TasksUpdateRequest;
import tech.engix.tasks_service.model.Tasks;

@Mapper(componentModel = "spring")
public interface TasksMapper {

    RequestTasks toEntity(Tasks tasks);
    Tasks toDto(RequestTasks tasks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TasksUpdateRequest toUpdate(Tasks save);
}
