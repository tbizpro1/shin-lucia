package com.shin.lucia.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaSummaryRequest {

    private Long companyId;
    private Long ideaId;
    private String objectName;
    private String urlFile;
    private Map<String, String> content;
}
