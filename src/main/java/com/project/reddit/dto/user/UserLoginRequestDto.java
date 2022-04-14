package com.project.reddit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {

    @Email
    private String email;

    @Size(min = 5, max = 20)
    private String password;

}
