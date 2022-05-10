package com.project.reddit.service;

import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.dto.user.login.UserLoginRequestDto;
import com.project.reddit.dto.user.login.UserLoginResponse;
import com.project.reddit.exception.BadRequestException;
import com.project.reddit.mapper.UserMapper;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.security.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class UserServicePostHandler {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserService userService;


    User testUser = new User(1L, "halco", "password", "email@email.com", LocalDate.now(), "image", UserRole.ROLE_USER);
    UserProfileDto testUserProfile = new UserProfileDto(testUser.getId(), testUser.getUsername(), testUser.getEmail(), testUser.getCreatedAt(), testUser.getImageUrl(), new ArrayList<>(), new ArrayList<>());
    UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto("email@emal.com", "password");

    @Test
    void signupUser() {
    }

    @Test
    void getUserProfileByUsername() {
        Mockito.when(userRepository.getUserByUsername("halco")).thenReturn(Optional.of(testUser));
        Mockito.when(userMapper.userProfileDto(testUser)).thenReturn(testUserProfile);

        var response = userService.getUserProfileByUsernameOrId("halco", null);

        Assertions.assertEquals(response, testUserProfile);
    }

    @Test
    void getUserProfileById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(userMapper.userProfileDto(testUser)).thenReturn(testUserProfile);

        var response = userService.getUserProfileByUsernameOrId(null, 1L);

        Assertions.assertEquals(response, testUserProfile);
    }

    @Test
    @DisplayName("Method getUserProfileByUsernameOrId should fail if username AND id are null")
    void failTestWhenUserProfileParametersAreNull(){
        var exception = assertThrows(BadRequestException.class, () -> {
            userService.getUserProfileByUsernameOrId(null,null);
        });
        assertEquals("Username and id are null", exception.getMessage());
    }

    @Test
    void getUserProfileWithJwt() {
    }

    @Test
    @DisplayName("Get User object by id")
    void getUserById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        var response = userService.getUserById(1L);
        Assertions.assertEquals(response, testUser);
    }

    @Test
    void userLogin() {
/*
        Mockito.when(userRepository.getUserByEmail("email@email.com")).thenReturn(Optional.of(testUser));

        assertNotNull(testUser);

        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken()))

        UserLoginResponse userLoginResponse = new UserLoginResponse();*/





    }

    @Test
    void getCurrentlyLoggedUser() {
    }
}