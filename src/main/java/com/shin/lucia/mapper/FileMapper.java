package com.shin.lucia.mapper;

import com.shin.lucia.dto.FileRequest;
import com.shin.lucia.dto.FileResponse;
import com.shin.lucia.entity.File;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public File toEntity(FileRequest request, Long userId) {
        return File.builder()
                .fileUrl(request.getFileUrl())
                .type(request.getType())
                .author(request.getAuthor())
                .step(request.getStep())
                .name(request.getName())
                .userId(userId)
                .build();
    }

    public FileResponse toResponse(File file) {
        return FileResponse.builder()
                .id(file.getId())
                .fileUrl(file.getFileUrl())
                .type(file.getType())
                .author(file.getAuthor())
                .step(file.getStep())
                .name(file.getName())
                .userId(file.getUserId())
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .build();
    }
}
