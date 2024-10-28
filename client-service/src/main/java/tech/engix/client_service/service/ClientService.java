package tech.engix.client_service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.engix.client_service.client.UserServiceClient;
import tech.engix.client_service.dto.ClientRequest;
import tech.engix.client_service.dto.ClientResponse;
import tech.engix.client_service.dto.ClientUpdateRequest;
import tech.engix.client_service.dto.UserResponse;
import tech.engix.client_service.mapper.ClientMapper;
import tech.engix.client_service.model.Client;
import tech.engix.client_service.repository.ClientRepository;
import tech.engix.client_service.service.exception.exceptions.AccessDeniedException;
import tech.engix.client_service.service.exception.exceptions.ClientNotFound;
import tech.engix.jwtutils.service.JwtUtilsService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientService {

    private final ClientRepository repository;

    private final UserServiceClient userServiceClient;

    private final ClientMapper mapper;

    private final JwtUtilsService jwtUtils;

    public List<Client> listAll(String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

        UserResponse userResponse = userServiceClient.getUserByEmail(email);

        if (userResponse != null) {
            List<Client> projects = repository.findByUserId(userResponse.id());

            return projects.stream()
                    .sorted(Comparator.comparing(Client::getId))
                    .toList();
        }

        return Collections.emptyList();
    }

    public Optional<ClientResponse> getName(Long id) {
        Client findById = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return Optional.ofNullable(mapper.toClientResponse(findById));
    }

    public ClientResponse createAClient(ClientRequest dto, String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse userResponse = userServiceClient.getUserByEmail(email);

        Client client = mapper.toEntity(dto);
        client.setUserId(userResponse.id());
        mapper.toProjectRequest(repository.save(client));
        ClientResponse a = mapper.toClientResponse(client);
        return mapper.toClientResponse(a);
    }

    public void editClient(ClientUpdateRequest update, Long id, String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse userResponse = userServiceClient.getUserByEmail(email);

        Optional<Client> optionalClient = repository.findById(id);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();

            if (client.getUserId().equals(userResponse.id())) {
                client.setName(update.name());

                Client updatedClient = repository.save(client);

                mapper.toUpdate(updatedClient);
            } else {
                throw new AccessDeniedException("Unauthorized: You do not have permission to edit this client.");
            }
        } else {
            throw new EntityNotFoundException("Client not found with id " + id);
        }
    }

    public void deleteClient(Long id, String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserResponse userResponse = userServiceClient.getUserByEmail(email);

        if (repository.existsByIdAndUserId(id, userResponse.id())) {
            repository.deleteById(id);
        } else {
            throw new ClientNotFound("Client not found or user not authorized to delete this client.");
        }
    }

}


