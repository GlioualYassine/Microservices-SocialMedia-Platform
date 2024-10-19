package org.example.commentservice.FeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "post-service",url = "http://localhost:8083/api/v1/posts")
public interface PostServiceClient {

    @GetMapping("/{id}")
    PostResponse  getPostById(@PathVariable("id") UUID id);

}
