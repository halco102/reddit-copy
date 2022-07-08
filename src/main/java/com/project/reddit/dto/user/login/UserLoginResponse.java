package com.project.reddit.dto.user.login;

import com.project.reddit.dto.user.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {

    private String jwt;

    //private UserProfileDto userProfileDto;

}
