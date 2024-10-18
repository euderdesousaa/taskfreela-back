package tech.engix.tasks_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.engix.tasks_service.model.Tasks;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Tasks, String> {
    List<Tasks> findByUserId(Long owner);
}
