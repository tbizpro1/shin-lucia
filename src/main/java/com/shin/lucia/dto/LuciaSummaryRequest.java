package com.shin.lucia.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaSummaryRequest {

    private String objectName;
    private String urlFile;
    private Long ideaId;
}
