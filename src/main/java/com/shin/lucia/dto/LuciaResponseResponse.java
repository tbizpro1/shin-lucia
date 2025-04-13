package com.shin.lucia.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaResponseResponse {

    private Long id;
    private Double relatedStep;
    private String content;
    private String author;
    private String urlHistory;
    private String objectName;
    private Long ideaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
