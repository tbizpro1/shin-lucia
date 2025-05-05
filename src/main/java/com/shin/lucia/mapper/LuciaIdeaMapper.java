package com.shin.lucia.mapper;

import com.shin.lucia.dto.LuciaIdeaRequest;
import com.shin.lucia.dto.LuciaIdeaResponse;
import com.shin.lucia.entity.LuciaIdea;

public class LuciaIdeaMapper {

    public static LuciaIdea toEntity(LuciaIdeaRequest request, Long companyId) {
        return LuciaIdea.builder()
                .companyId(companyId)
                .title(request.getTitle())
                .description(request.getDescription())
                .step(request.getStep())
                .problem(request.getProblem())
                .solution(request.getSolution())
                .whoIs(request.getWhoIs())
                .build();
    }

    public static LuciaIdeaResponse toResponse(LuciaIdea idea) {
        return LuciaIdeaResponse.builder()
                .id(idea.getId())
                .companyId(idea.getCompanyId())
                .title(idea.getTitle())
                .description(idea.getDescription())
                .step(idea.getStep())
                .problem(idea.getProblem())
                .solution(idea.getSolution())
                .whoIs(idea.getWhoIs())
                .createdAt(idea.getCreatedAt())
                .updatedAt(idea.getUpdatedAt())
                .build();
    }
}
