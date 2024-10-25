package tech.engix.client_service.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.engix.client_service.dto.ClientRequest;
import tech.engix.client_service.dto.ClientResponse;
import tech.engix.client_service.dto.ClientUpdateRequest;
import tech.engix.client_service.model.Client;
import tech.engix.client_service.service.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping()
    public ResponseEntity<List<Client>> listAllTasks(
            HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(service.listAll( jwtToken));
    }

    @GetMapping("/name")
    @Hidden
    public ResponseEntity<Optional<ClientResponse>> getClientByName(@RequestParam("id") Long id){
        Optional<ClientResponse> client = service.getName(id);
        if (client.isPresent()) {
            return ResponseEntity.ok(client);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ClientRequest> createClient(@RequestBody ClientRequest dto, HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ClientRequest createdClient = service.createAClient(dto, jwtToken);
        return ResponseEntity.ok(createdClient);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ClientUpdateRequest> edit(@PathVariable Long id,
                                                    @RequestBody ClientUpdateRequest client,
                                                    HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);
        service.editClient(client, id, jwtToken);
        return ResponseEntity.ok().body(client);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request) {
        String jwtToken = getJwtFromCookies(request);
        service.deleteClient(id, jwtToken);
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
