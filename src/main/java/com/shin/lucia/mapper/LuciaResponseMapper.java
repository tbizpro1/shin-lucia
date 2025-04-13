package com.shin.lucia.mapper;

import com.shin.lucia.dto.LuciaResponseRequest;
import com.shin.lucia.dto.LuciaResponseResponse;
import com.shin.lucia.entity.LuciaIdea;
import com.shin.lucia.entity.LuciaResponse;

public class LuciaResponseMapper {

    public static LuciaResponse toEntity(LuciaResponseRequest request, LuciaIdea idea) {
        return LuciaResponse.builder()
                .relatedStep(request.getRelatedStep())
                .content(request.getContent())
                .author(request.getAuthor())
                .urlHistory(request.getUrlHistory())
                .objectName(request.getObjectName())
                .idea(idea)
                .build();
    }

    public static LuciaResponseResponse toResponse(LuciaResponse response) {
        return LuciaResponseResponse.builder()
                .id(response.getId())
                .relatedStep(response.getRelatedStep())
                .content(response.getContent())
                .author(response.getAuthor())
                .urlHistory(response.getUrlHistory())
                .objectName(response.getObjectName())
                .ideaId(response.getIdea().getId())
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .build();
    }
}
