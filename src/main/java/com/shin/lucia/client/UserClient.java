package com.shin.lucia.client;

import com.shin.lucia.config.FeignClientConfig;
import com.shin.lucia.dto.UserMinimalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service", path = "/api/v1/users", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/{id}/minimal")
    UserMinimalResponse getUserMinimalById(@PathVariable("id") Long id);

    @GetMapping("/id-by-username/{username}")
    Long findIdByUsername(@PathVariable("username") String username);
}

