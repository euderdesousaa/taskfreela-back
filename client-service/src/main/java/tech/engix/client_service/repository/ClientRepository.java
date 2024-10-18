package tech.engix.client_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.engix.client_service.model.Client;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByIdAndUserId(Long id, Long userId);

    List<Client> findByUserId(Long userId);
}
