package com.zelkova.zelkova.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTO {
    private String nickname;
    
    private String email;
    
    private String profileImageName;
}

