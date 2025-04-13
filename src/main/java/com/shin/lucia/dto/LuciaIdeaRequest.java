package com.shin.lucia.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LuciaIdeaRequest {

    private String title;
    private String description;
    private Double step;
    private String problem;
    private String solution;
    private String whoIs;
}
