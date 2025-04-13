package com.shin.lucia.controller;

import com.shin.lucia.dto.LuciaResponseRequest;
import com.shin.lucia.dto.LuciaResponseResponse;
import com.shin.lucia.service.LuciaResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lucia/responses")
@RequiredArgsConstructor
public class LuciaResponseController {

    private final LuciaResponseService responseService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public LuciaResponseResponse create(@RequestBody LuciaResponseRequest request) {
        return responseService.create(request);
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
