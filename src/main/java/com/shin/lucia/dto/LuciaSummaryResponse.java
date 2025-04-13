package com.shin.lucia.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaSummaryResponse {

    private Long id;
    private String objectName;
    private String urlFile;
    private Long ideaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
