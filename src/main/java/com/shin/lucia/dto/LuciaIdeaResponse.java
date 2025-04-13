package com.shin.lucia.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaIdeaResponse {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Double step;
    private String problem;
    private String solution;
    private String whoIs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
