package com.shin.lucia.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shin.lucia.dto.LuciaResponseResponse;
import com.shin.lucia.entity.LuciaResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LuciaResponseMapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static LuciaResponseResponse toResponse(LuciaResponse entity) {
        List<Map<String, Object>> contentList;

        try {
            contentList = mapper.readValue(entity.getContent(), new TypeReference<>() {});
        } catch (Exception e) {
            contentList = Collections.emptyList();
        }

        return LuciaResponseResponse.builder()
                .id(entity.getId())
                .relatedStep(entity.getRelatedStep())
                .content(contentList)
                .author(entity.getAuthor())
                .urlHistory(entity.getUrlHistory())
                .objectName(entity.getObjectName())
                .ideaId(entity.getIdea().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
