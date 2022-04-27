package com.project.reddit.dto.user.signup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupResponseDto {

    private Long id;

    private String username;

    private String email;

    private String imageUrl;

}
