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
    @PutMapping("/idea/{ideaId}/step/{step}")
    public LuciaResponseResponse uploadStepResponseAsText(
            @PathVariable Long ideaId,
            @PathVariable Double step,
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) throws IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        return responseService.uploadResponseAsTxt(ideaId, step, data, username);
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
