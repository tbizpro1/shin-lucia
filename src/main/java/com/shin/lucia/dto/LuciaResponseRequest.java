package com.shin.lucia.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaResponseRequest {

    private Double relatedStep;
    private String content;
    private String author;
    private String urlHistory;
    private String objectName;
    private Long ideaId;
}
