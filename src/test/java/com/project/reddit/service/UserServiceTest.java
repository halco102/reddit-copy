package com.project.reddit.service;

import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.mapper.UserMapper;
import com.project.reddit.mapper.UserMapperImpl;
import com.project.reddit.model.user.User;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.security.CustomUserDetailsImp;
import com.project.reddit.security.JwtTokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        UserMapperImpl.class,
        JwtTokenUtil.class,
        CustomUserDetailsImp.class
})
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private UserService userService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable =  MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, userMapper, authenticationManager, passwordEncoder, jwtTokenUtil);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void signupUser() {
        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setUsername("username");
        userSignupRequestDto.setPassword("123123123");
        userSignupRequestDto.setEmail("email@email.com");

        var userMap = userMapper.signupToEntity(userSignupRequestDto);
        userMap.setPosts(new ArrayList<>());
        userMap.setLikeDislikes(new ArrayList<>());
        userMap.setImageUrl("Image url");

        userRepository.save(userMap);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(argumentCaptor.capture());

        var capture = argumentCaptor.getValue();

        assertThat(capture).isEqualTo(userMap);

    }

    @Test
    void canGetUserByUsername() {
        //when
        userRepository.getUserByUsername("halco");
        verify(userRepository).getUserByUsername("halco");
    }

    @Test
    void canGetUserByEmail() {
        userRepository.getUserByEmail("mock");
        verify(userRepository).getUserByEmail("mock");
    }


    @Test
    void getUserById() {
    }

    @Test
    void userLogin() {
    }

    @Test
    void getCurrentlyLoggedUser() {
    }
}