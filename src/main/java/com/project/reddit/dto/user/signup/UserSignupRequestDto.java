package com.project.reddit.dto.user.signup;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 30)
    private String password;

    @NotNull
    @NotBlank
    @Email
    private String email;

}
