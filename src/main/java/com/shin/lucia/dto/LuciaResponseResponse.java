package com.shin.lucia.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaResponseResponse {

    private Long id;
    private Double relatedStep;
    private List<Map<String, Object>> content;
    private String author;
    private String urlHistory;
    private String objectName;
    private Long ideaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
