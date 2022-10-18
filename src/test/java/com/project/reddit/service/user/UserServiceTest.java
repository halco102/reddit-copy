/*
package com.project.reddit.service.user;

import com.project.reddit.dto.post.PostForFrontPageDto;
import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.dto.user.login.UserLoginRequestDto;
import com.project.reddit.dto.user.login.UserLoginResponse;
import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.exception.BadRequestException;
import com.project.reddit.exception.DuplicateException;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.exception.Unauthorized;
import com.project.reddit.mapper.AbstractCommentMapper;
import com.project.reddit.mapper.AbstractUserMapper;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.security.JwtTokenUtil;
import com.project.reddit.service.search.Search;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AbstractUserMapper userMapper;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Mock
    JavaMailSender mailSender;

    @Mock
    Search<User> search;

    @Mock
    AbstractCommentMapper commentMapper;

    @InjectMocks
    UserService userService;

    private final String RANDOM_AVATAR_URL="https://avatars.dicebear.com/api/bottts/";

    //private SecurityContextHolder securityContext = Mockito.mock(SecurityContextHolder.class);

    private Authentication authentication = Mockito.mock(Authentication.class);

    private User userObject;
    private UserSignupRequestDto userSignupRequestDto;

    @BeforeEach
    void beforeEach() {
        when(userMapper.signupToEntity(Mockito.any()))
                .thenReturn(new User("halco1002", "987456321aa", "email@email.com" ));

        userObject = new User(1L, "halco1002", "987456321aa", "email.@email.com", LocalDate.now(), "image", UserRole.ROLE_USER);
        userSignupRequestDto = new UserSignupRequestDto("halco1002", "987456321aa", "email@email.com");
    }

    @Test
    void testDuplicateExceptionOnSignup() {
        when(userRepository.getUserByEmail("email@email.com")).thenReturn(Optional.ofNullable(userObject));

        Assertions.assertThrows(DuplicateException.class, () -> userService.signupUser(userSignupRequestDto));

        when(userRepository.getUserByUsername("halco1002")).thenReturn(Optional.ofNullable(userObject));
        Assertions.assertThrows(DuplicateException.class, () -> userService.signupUser(userSignupRequestDto));
    }

*/
/*    @Test
    void testSignupUser() {

        var user = userMapper.signupToEntity(userSignupRequestDto);

        user.setId(1L);
        user.setCreatedAt(LocalDate.now());
        user.setImageUrl(RANDOM_AVATAR_URL + UUID.randomUUID() + ".svg");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.ROLE_USER);
        user.setVerificationCode(String.valueOf(UUID.randomUUID()));

        // not sure how to get into the private method, bad design ? Maybe change it to public (sendVerificationEmail)



    }*//*


    @Test
    void testVerifieUserViaEmail() {

        when(userRepository.verifieUserCode(Mockito.anyString())).thenReturn(Optional.ofNullable(userObject));

        when(userRepository.save(Mockito.any())).thenReturn(userObject);

        var verify = userService.verifieUserViaEmail("123");

        var saveUser = userRepository.save(userObject);

        Assertions.assertNotNull(verify);

        Assertions.assertNotNull(saveUser);

        Assertions.assertEquals("Verified continue to vue website http://localhost:8081/", verify);

        Assertions.assertEquals(userObject.getId(), saveUser.getId());

    }

    @Test
    void testBadRequestExceptionOnVerifyEmail() {
        when(userRepository.verifieUserCode(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(BadRequestException.class, () -> userService.verifieUserViaEmail("123"));
    }

    @Test
    void getUserById() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userObject));

        var user = userService.getUserById(1L);
        Assertions.assertDoesNotThrow(() -> NotFoundException.class, "No exception");
        Assertions.assertEquals(userObject.getUsername(), user.getUsername());
        Assertions.assertEquals(userObject.getEmail(), user.getEmail());
        Assertions.assertEquals(userObject.getId(), user.getId());
    }

    @Test
    void testUserLogin() {

        when(userRepository.getUserByUsername(userObject.getUsername())).thenReturn(Optional.ofNullable(userObject));

        when(jwtTokenUtil.generateJwtToken(Mockito.any())).thenReturn("123");

        when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        when(userMapper.userLoginResponseDto("123")).thenReturn(new UserLoginResponse("123"));

        //set verified email to true
        userObject.setVerified(true);

        var temp = userService.userLogin(new UserLoginRequestDto("halco1002", "987456321aa"));

        Assertions.assertEquals("123", temp.getJwt());
    }

    @Test
    void throwNotFoundExceptionOnUserLoginWhenTheEmailOrUsernameDoesNotExist() {
        //username
        Assertions.assertThrows(NotFoundException.class,() -> userService.userLogin(new UserLoginRequestDto("wrongUsername", "987456321aa")));

        Assertions.assertThrows(NotFoundException.class,() -> userService.userLogin(new UserLoginRequestDto("wrongEmail@email.com", "password123")));

    }

    @Test
    void throwExceptionOnWrongPassword() {
        when(userRepository.getUserByUsername("halco1002")).thenReturn(Optional.ofNullable(userObject));

        Assertions.assertNotEquals(userObject.getPassword(), "wrongpassword");
        Assertions.assertThrows(Unauthorized.class, () -> userService.userLogin(new UserLoginRequestDto("halco1002", "wrongpass")));
    }

    @Test
    void getAllPostsByUser() {
        List<Post> userPosts = new ArrayList<>(Arrays.asList(
                //Long id, String title, String text, String imageUrl, boolean allowComments, User user
                new Post(1L, "first", "text", "image", true, new User()),
                new Post(2L, "second", "", "image", false, new User())
        ));



        List<PostForFrontPageDto> post = new ArrayList<>();

        userPosts.forEach(e -> post.add(new PostForFrontPageDto(e.getId(), e.getTitle(), e.getText(), e.getImageUrl(), new ArrayList<>(), new HashSet<>())));

        userObject.setPosts(userPosts);

        when(userRepository.getAllPostByUserId(1L)).thenReturn(Optional.of(userObject));

        when(userMapper.userProfileDto(userObject)).thenReturn(new UserProfileDto(userObject.getId(), userObject.getUsername(),
                userObject.getEmail(), userObject.getCreatedAt(),
                userObject.getImageUrl(), post, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        var userProfile = userService.getAllPostsByUser(1L);

        Assertions.assertDoesNotThrow(() -> NotFoundException.class);

        Assertions.assertEquals(userObject.getId(), userProfile.getId());

        Assertions.assertEquals(userObject.getUsername(), userProfile.getUsername());

        Assertions.assertEquals(userObject.getPosts().size(), userProfile.getPosts().size());
    }

}*/
