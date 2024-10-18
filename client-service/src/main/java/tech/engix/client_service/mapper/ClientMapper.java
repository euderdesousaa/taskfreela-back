package tech.engix.client_service.mapper;

import org.mapstruct.Mapper;
import tech.engix.client_service.dto.ClientRequest;
import tech.engix.client_service.model.Client;
import tech.engix.client_service.dto.ClientUpdateRequest;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientRequest clientRequest);
    ClientRequest toProjectRequest(Client client);

    ClientUpdateRequest toUpdate(Client save);
}
