package com.project.reddit.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {

    @NotNull
    @NotBlank
    @Range(min = 3, max = 30)
    private String username;

    @NotBlank
    @NotNull
    @Range(min = 8, max = 30)
    private String password;

    @NotNull
    @NotBlank
    @Email
    private String email;

}
