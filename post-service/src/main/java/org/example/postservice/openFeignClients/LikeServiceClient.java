package org.example.postservice.openFeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "like-service" , url = "http://localhost:8086/api/v1/")
public interface LikeServiceClient {
    @GetMapping("/likes/post/{postId}")
    List<LikeResponse> getLikesByPostId(@PathVariable UUID postId);
}
