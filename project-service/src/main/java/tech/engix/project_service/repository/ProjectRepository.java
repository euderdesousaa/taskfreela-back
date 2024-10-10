package tech.engix.project_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.engix.project_service.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
