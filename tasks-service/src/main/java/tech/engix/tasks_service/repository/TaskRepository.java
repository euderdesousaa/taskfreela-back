package tech.engix.tasks_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.engix.tasks_service.model.Tasks;
import tech.engix.tasks_service.model.enums.Status;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Tasks, String> {
    List<Tasks> findByUserId(Long owner);
    List<Tasks> findByProjectId(Long projectId);
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
    boolean existsByIdAndUserId(String id, Long userId);

    long countByProjectId(Long projectId);

    long countByProjectIdAndStatus(Long projectId, Status status);
}
