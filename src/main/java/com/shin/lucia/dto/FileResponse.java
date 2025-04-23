package com.shin.lucia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {

    private Long id;
    private String fileUrl;
    private String type;
    private String author;
    private String name;
    private Long userId;
    private Long ideaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
