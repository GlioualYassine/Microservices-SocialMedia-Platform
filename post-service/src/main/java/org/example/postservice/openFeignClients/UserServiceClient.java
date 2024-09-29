package org.example.postservice.openFeignClients;

import org.example.postservice.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service",url = "http://localhost:8082/api/v1/users")

public interface UserServiceClient {

    @GetMapping("/{id}")
    User getUserById(@PathVariable("id") UUID id);

}
