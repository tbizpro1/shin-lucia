package com.shin.lucia.mapper;

import com.shin.lucia.dto.LuciaIdeaRequest;
import com.shin.lucia.dto.LuciaIdeaResponse;
import com.shin.lucia.entity.LuciaIdea;

public class LuciaIdeaMapper {

    public static LuciaIdea toEntity(LuciaIdeaRequest request, Long userId) {
        return LuciaIdea.builder()
                .userId(userId)
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
                .userId(idea.getUserId())
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
