package com.shin.lucia.controller;

import com.shin.lucia.client.CompanyClient;
import com.shin.lucia.dto.LuciaIdeaRequest;
import com.shin.lucia.dto.LuciaIdeaResponse;
import com.shin.lucia.security.JwtService;
import com.shin.lucia.service.LuciaIdeaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lucia/ideas")
@RequiredArgsConstructor
public class LuciaIdeaController {

    private final LuciaIdeaService ideaService;
    private final JwtService jwtService;
    private final CompanyClient companyClient;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public LuciaIdeaResponse create(@RequestBody LuciaIdeaRequest request, HttpServletRequest req) {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        Long companyId = companyClient.getMyCompanyId(token);
        return ideaService.create(request, companyId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-company/{companyId}")
    public List<LuciaIdeaResponse> getByCompanyId(@PathVariable Long companyId) {
        return ideaService.getByCompanyId(companyId);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public LuciaIdeaResponse getById(@PathVariable Long id) {
        return ideaService.getById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/company")
    public List<LuciaIdeaResponse> getByCompany(HttpServletRequest req) {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        Long companyId = companyClient.getMyCompanyId(token);
        return ideaService.getByCompanyId(companyId);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public LuciaIdeaResponse update(@PathVariable Long id, @RequestBody LuciaIdeaRequest request) {
        return ideaService.update(id, request);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ideaService.delete(id);
    }
}
