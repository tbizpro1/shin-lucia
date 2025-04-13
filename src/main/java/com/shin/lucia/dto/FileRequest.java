package com.shin.lucia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRequest {

    private String fileUrl;
    private String type;
    private String author;
    private Double step;
    private String name;
}
