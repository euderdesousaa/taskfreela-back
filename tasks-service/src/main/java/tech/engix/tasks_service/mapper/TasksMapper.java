package tech.engix.tasks_service.mapper;

import org.mapstruct.Mapper;
import tech.engix.tasks_service.dto.RequestTasks;
import tech.engix.tasks_service.dto.TasksUpdateRequest;
import tech.engix.tasks_service.model.Tasks;

@Mapper(componentModel = "spring")
public interface TasksMapper {

    RequestTasks toEntity(Tasks tasks);
    Tasks toDto(RequestTasks tasks);

    TasksUpdateRequest toUpdate(Tasks save);
}
