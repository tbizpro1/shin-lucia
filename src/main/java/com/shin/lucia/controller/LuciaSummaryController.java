package com.shin.lucia.controller;

import com.shin.lucia.dto.LuciaSummaryResponse;
import com.shin.lucia.security.JwtService;
import com.shin.lucia.service.LuciaSummaryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    @PostMapping("/company/{companyId}/idea/{ideaId}/upload-json")
    public LuciaSummaryResponse createSummaryFromJson(
            @PathVariable Long companyId,
            @PathVariable Long ideaId,
            @RequestBody Map<String, String> summaryBody
    ) throws IOException {
        if (summaryBody == null || summaryBody.isEmpty()) {
            throw new IllegalArgumentException("O corpo da requisição não pode estar vazio.");
        }
        return summaryService.createSummaryFromJson(companyId, ideaId, summaryBody);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/company/{companyId}/idea/{ideaId}/upload-json")
    public LuciaSummaryResponse updateSummaryFromJson(
            @PathVariable Long companyId,
            @PathVariable Long ideaId,
            @RequestBody Map<String, String> summaryBody
    ) throws IOException {
        if (summaryBody == null || summaryBody.isEmpty()) {
            throw new IllegalArgumentException("O corpo da requisição não pode estar vazio.");
        }
        return summaryService.updateSummaryFromJson(companyId, ideaId, summaryBody);
    }


    @PutMapping("/company/{companyId}/idea/{ideaId}/upload-file")
    @PreAuthorize("isAuthenticated()")
    public LuciaSummaryResponse updateWithFile(
            @PathVariable Long companyId,
            @PathVariable Long ideaId,
            @RequestPart("file") MultipartFile file
    ) {
        return summaryService.updateWithFile(companyId, ideaId, file);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        summaryService.delete(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/company/{companyId}/idea/{ideaId}")
    public LuciaSummaryResponse getByIdea(
            @PathVariable Long companyId,
            @PathVariable Long ideaId
    ) {
        return summaryService.getByIdeaId(companyId, ideaId);
    }
}
