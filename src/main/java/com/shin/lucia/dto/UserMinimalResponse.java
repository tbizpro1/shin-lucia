package com.shin.lucia.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMinimalResponse {
    private Long id;
    private String email;
    private String fullName;
    private String role;
}
