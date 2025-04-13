package com.shin.lucia.controller;

import com.shin.lucia.dto.LuciaSummaryRequest;
import com.shin.lucia.dto.LuciaSummaryResponse;
import com.shin.lucia.service.LuciaSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lucia/summary")
@RequiredArgsConstructor
public class LuciaSummaryController {

    private final LuciaSummaryService summaryService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public LuciaSummaryResponse save(@RequestBody LuciaSummaryRequest request) {
        return summaryService.createOrUpdate(request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/idea/{ideaId}")
    public LuciaSummaryResponse getByIdea(@PathVariable Long ideaId) {
        return summaryService.getByIdeaId(ideaId);
    }
}
