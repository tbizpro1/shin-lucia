package com.shin.lucia.controller;

import com.shin.lucia.dto.LuciaSummaryRequest;
import com.shin.lucia.dto.LuciaSummaryResponse;
import com.shin.lucia.security.JwtService;
import com.shin.lucia.service.LuciaSummaryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lucia/summary")
@RequiredArgsConstructor
public class LuciaSummaryController {

    private final LuciaSummaryService summaryService;
    private final JwtService jwtService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/idea/{ideaId}/upload-json")
    public LuciaSummaryResponse createSummaryFromJson(
            @PathVariable Long ideaId,
            @RequestBody Map<String, String> steps,
            HttpServletRequest httpRequest
    ) throws IOException {
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        return summaryService.createSummaryFromJson(ideaId, steps, username);
    }

    @PutMapping("/idea/{ideaId}/upload-json")
    public LuciaSummaryResponse updateSummaryFromJson(
            @PathVariable Long ideaId,
            @RequestBody Map<String, String> steps,
            HttpServletRequest httpRequest
    ) throws IOException {
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        return summaryService.generateAndUploadSummaryFile(ideaId, steps, username);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        summaryService.delete(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/idea/{ideaId}")
    public LuciaSummaryResponse getByIdea(@PathVariable Long ideaId) {
        return summaryService.getByIdeaId(ideaId);
    }
}
