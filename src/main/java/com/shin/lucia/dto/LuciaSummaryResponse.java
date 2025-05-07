package com.shin.lucia.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaSummaryResponse {

    private Long id;
    private Long companyId;
    private Long ideaId;
    private String objectName;
    private String urlFile;
    private Map<String, String> content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
