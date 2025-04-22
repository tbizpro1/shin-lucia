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

@RestController
@RequestMapping("/api/v1/lucia/summary")
@RequiredArgsConstructor
public class LuciaSummaryController {

    private final LuciaSummaryService summaryService;
    private final JwtService jwtService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LuciaSummaryResponse createWithFile(
            @RequestPart("data") LuciaSummaryRequest request,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest httpRequest
    ) throws IOException {
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        return summaryService.createOrUpdateWithFile(request, file, username);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public LuciaSummaryResponse update(
            @PathVariable Long id,
            @RequestBody LuciaSummaryRequest request
    ) {
        return summaryService.update(id, request);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LuciaSummaryResponse updateWithFile(
            @PathVariable Long id,
            @RequestPart(value = "data", required = false) LuciaSummaryRequest request,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest httpRequest
    ) throws IOException {
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        return summaryService.updateWithFile(id, request, file, username);
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
