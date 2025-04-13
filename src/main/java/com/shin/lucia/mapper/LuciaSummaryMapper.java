package com.shin.lucia.mapper;

import com.shin.lucia.dto.LuciaSummaryRequest;
import com.shin.lucia.dto.LuciaSummaryResponse;
import com.shin.lucia.entity.LuciaIdea;
import com.shin.lucia.entity.LuciaSummaryIdeas;

public class LuciaSummaryMapper {

    public static LuciaSummaryIdeas toEntity(LuciaSummaryRequest request, LuciaIdea idea) {
        return LuciaSummaryIdeas.builder()
                .objectName(request.getObjectName())
                .urlFile(request.getUrlFile())
                .idea(idea)
                .build();
    }

    public static LuciaSummaryResponse toResponse(LuciaSummaryIdeas summary) {
        return LuciaSummaryResponse.builder()
                .id(summary.getId())
                .objectName(summary.getObjectName())
                .urlFile(summary.getUrlFile())
                .ideaId(summary.getIdea().getId())
                .createdAt(summary.getCreatedAt())
                .updatedAt(summary.getUpdatedAt())
                .build();
    }
}
