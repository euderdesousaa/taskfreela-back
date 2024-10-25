package tech.engix.client_service.mapper;

import org.mapstruct.Mapper;
import tech.engix.client_service.dto.ClientRequest;
import tech.engix.client_service.dto.ClientResponse;
import tech.engix.client_service.dto.ClientUpdateRequest;
import tech.engix.client_service.model.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientRequest clientRequest);
    ClientRequest toProjectRequest(Client client);

    ClientUpdateRequest toUpdate(Client save);

    ClientResponse toClientResponse(Client client);
}
