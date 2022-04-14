package com.project.reddit.service;

import com.project.reddit.dto.user.*;
import com.project.reddit.exception.BadRequestException;
import com.project.reddit.exception.DuplicateException;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.exception.Unauthorized;
import com.project.reddit.mapper.UserMapper;
import com.project.reddit.model.user.User;
import com.project.reddit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final String RANDOM_AVATAR_URL="https://avatars.dicebear.com/api/bottts/";

    public UserSignupResponseDto signupUser(UserSignupRequestDto signupRequest) {

        var user = userMapper.signupToEntity(signupRequest);
        user.setCreatedAt(LocalDate.now());
        user.setImageUrl(RANDOM_AVATAR_URL + UUID.randomUUID() + ".svg");

        if(userRepository.getUserByUsername(signupRequest.getUsername()).isPresent()) {
            throw new DuplicateException("Username is already taken");
        }else if (userRepository.getUserByEmail(signupRequest.getEmail()).isPresent()) {
            throw new DuplicateException("Email is already taken");
        }

        var saveEntity = userRepository.save(user);
        log.info("User successfully saved to db");
        return userMapper.signupResponseDto(saveEntity);
    }

    private UserProfileDto fetchUserProfileById(Long id) {
        var userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new NotFoundException("The user with id: " + id + " does not exist");
        }
        return userMapper.userProfileDto(userById.get());
    }

    private UserProfileDto fetchUserProfileByUsername(String username) {

        if (username.isBlank() || username.isEmpty()) {
            log.error("Username is blank or is empty");
            throw new BadRequestException("Username is blank or empty");
        }

        var userByUsername = userRepository.getUserByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new NotFoundException("The user with username: " + username + " does not exist!");
        }
        return userMapper.userProfileDto(userByUsername.get());
    }

    public UserProfileDto getUserProfileByUsernameOrId(String username, Long id) {

        if (id != null) {
            return fetchUserProfileById(id);
        }else if (username != null) {
            return fetchUserProfileByUsername(username);
        }else {
            log.info("Both are null");
            throw new BadRequestException("Username and id are null");
        }
    }

    public User getUserById(Long id) {
        var user = this.userRepository.findById(id);
        return user.orElseThrow(() -> {throw new NotFoundException("The user with id: " + id + " does not exist");});
    }

    public BasicUserInfo userLogin(UserLoginRequestDto requestDto) {

        var getUserByEmail = this.userRepository.getUserByEmail(requestDto.getEmail());

        if(getUserByEmail.isEmpty()){
            throw new NotFoundException("The user with email: " + requestDto.getEmail() + " does not exist");
        }

        if (getUserByEmail.get().getPassword().matches(requestDto.getPassword())) {
            log.info("Password match");
            return userMapper.fromEntityToBasicUserInfoDto(getUserByEmail.get());
        }else {
            throw new Unauthorized("The password does not match");
        }

    }


}
