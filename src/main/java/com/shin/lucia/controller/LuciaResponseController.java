package com.shin.lucia.controller;

import com.shin.lucia.dto.LuciaResponseRequest;
import com.shin.lucia.dto.LuciaResponseResponse;
import com.shin.lucia.security.JwtService;
import com.shin.lucia.service.LuciaResponseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lucia/responses")
@RequiredArgsConstructor
public class LuciaResponseController {

    private final LuciaResponseService responseService;
    private final JwtService jwtService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LuciaResponseResponse createWithFile(
            @RequestPart("data") LuciaResponseRequest request,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest httpRequest
    ) throws IOException {
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        return responseService.createWithFile(request, file, username);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public LuciaResponseResponse update(
            @PathVariable Long id,
            @RequestBody LuciaResponseRequest request
    ) {
        return responseService.update(id, request);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LuciaResponseResponse updateWithFile(
            @PathVariable Long id,
            @RequestPart(value = "data", required = false) LuciaResponseRequest request,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest httpRequest
    ) throws IOException {
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        return responseService.updateWithFile(id, request, file, username);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        responseService.delete(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public LuciaResponseResponse getById(@PathVariable Long id) {
        return responseService.getById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/idea/{ideaId}")
    public List<LuciaResponseResponse> getByIdea(@PathVariable Long ideaId) {
        return responseService.getByIdeaId(ideaId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/idea/{ideaId}/step-summaries")
    public Map<Double, String> getStepSummaries(@PathVariable Long ideaId) {
        return responseService.getStepSummaries(ideaId);
    }
}
