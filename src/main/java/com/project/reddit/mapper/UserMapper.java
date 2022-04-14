package com.project.reddit.mapper;

import com.project.reddit.dto.user.*;
import com.project.reddit.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User signupToEntity (UserSignupRequestDto signupRequestDto);

    UserSignupResponseDto signupResponseDto(User entity);

    @Mapping(target = "posts", ignore = true)
    UserProfileDto userProfileDto(User user);

    BasicUserInfo fromEntityToBasicUserInfoDto(User user);
}
