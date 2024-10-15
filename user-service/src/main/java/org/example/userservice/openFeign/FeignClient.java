package org.example.userservice.openFeign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@org.springframework.cloud.openfeign.FeignClient(name = "friendship-service" , url = "http://localhost:8589/api/v1/friendships")
public interface FeignClient {
    @GetMapping("/{userId}")
    UserDTO getUserById(@PathVariable UUID userId);
}
