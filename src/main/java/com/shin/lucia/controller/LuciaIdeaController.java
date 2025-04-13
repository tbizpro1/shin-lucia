package com.shin.lucia.controller;

import com.shin.lucia.client.UserClient;
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
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lucia/ideas")
@RequiredArgsConstructor
public class LuciaIdeaController {

    private final LuciaIdeaService ideaService;
    private final JwtService jwtService;
    private final UserClient userClient;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public LuciaIdeaResponse create(@RequestBody LuciaIdeaRequest request, HttpServletRequest req) {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        return ideaService.create(request, userId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public LuciaIdeaResponse getById(@PathVariable Long id) {
        return ideaService.getById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public List<LuciaIdeaResponse> getByUser(HttpServletRequest req) {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        return ideaService.getByUserId(userId);
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
