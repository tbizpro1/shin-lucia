package com.shin.lucia.service;

import com.shin.lucia.client.CompanyClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyClient companyClient;

    public CompanyService(CompanyClient companyClient) {
        this.companyClient = companyClient;
    }

    public Long getMyCompanyId(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        return companyClient.getMyCompanyId(token);
    }
}