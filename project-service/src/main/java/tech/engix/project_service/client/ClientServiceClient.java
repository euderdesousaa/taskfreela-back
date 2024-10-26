package tech.engix.project_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.engix.project_service.dto.ClientResponse;

@FeignClient(url = "${tech.engix.service.url}", name = "client-service")
public interface ClientServiceClient {

    @GetMapping("api/v1/client/name")
    ClientResponse getClientName(@RequestParam("id") Long id);
}
