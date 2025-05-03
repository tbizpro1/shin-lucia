package com.shin.lucia.client;

import com.shin.lucia.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "company-service",
        path = "/api/v1/companies",
        configuration = FeignClientConfig.class
)
public interface CompanyClient {

    @GetMapping("/my-company-id")
    Long getMyCompanyId(@RequestHeader("Authorization") String token);
}
