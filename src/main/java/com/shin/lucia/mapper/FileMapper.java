package com.shin.lucia.mapper;

import com.shin.lucia.dto.FileRequest;
import com.shin.lucia.dto.FileResponse;
import com.shin.lucia.entity.File;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public File toEntity(FileRequest request) {
        return File.builder()
                .fileUrl(null)
                .type(request.getType())
                .author(request.getAuthor())
                .name(request.getName())
                .ideaId(request.getIdeaId())
                .build();
    }

    public FileResponse toResponse(File file) {
        return FileResponse.builder()
                .id(file.getId())
                .fileUrl(file.getFileUrl())
                .type(file.getType())
                .author(file.getAuthor())
                .ideaId(file.getIdeaId())
                .userId(file.getUserId())
                .companyId(file.getCompanyId())
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .name(file.getName())
                .build();
    }


}
