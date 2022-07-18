package com.project.reddit.service.user;

import com.project.reddit.dto.post.PostForFrontPageDto;
import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.dto.user.login.UserLoginRequestDto;
import com.project.reddit.dto.user.login.UserLoginResponse;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.exception.Unauthorized;
import com.project.reddit.mapper.CommentMapper;
import com.project.reddit.mapper.UserMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    UserMapper userMapper;

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
    CommentMapper commentMapper;

    @InjectMocks
    UserService userService;

    private final String RANDOM_AVATAR_URL="https://avatars.dicebear.com/api/bottts/";

    //private SecurityContextHolder securityContext = Mockito.mock(SecurityContextHolder.class);

    private Authentication authentication = Mockito.mock(Authentication.class);

    private User userObject;

    @BeforeEach
    void beforeEach() {
        when(userMapper.signupToEntity(Mockito.any()))
                .thenReturn(new User("halco1002", "987456321aa", "ado.halilovic@outlook.com" ));

        userObject = new User(1L, "halco1002", "987456321aa", "ado.halilovic@outlook.com", LocalDate.now(), "image", UserRole.ROLE_USER);
    }


 /*   @Test
    void signupUser() {

        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto("halco1002", "ado.halilovic@outlook.com", "987456321aa");

        var user = userMapper.signupToEntity(userSignupRequestDto);

        user.setId(1L);
        user.setCreatedAt(LocalDate.now());
        user.setImageUrl(RANDOM_AVATAR_URL + UUID.randomUUID() + ".svg");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.ROLE_USER);
        user.setVerificationCode(String.valueOf(UUID.randomUUID()));
        AtomicReference<UserSignupResponseDto> userSignupResponseDto = new AtomicReference<>();


        Assertions.assertThrows(IllegalArgumentException.class, () -> userSignupResponseDto.set(userService.signupUser(userSignupRequestDto)), "Expected throw bcs private method");

        Assertions.assertNotNull(userSignupResponseDto.get());
        Assertions.assertEquals(user.getEmail(), userSignupResponseDto.get().getEmail());
        Assertions.assertEquals(user.getUsername(), userSignupResponseDto.get().getUsername());



    }*/

    @Test
    void verifieUserViaEmail() {
    }

    @Test
    void getUserProfileByUsernameOrId() {
    }

    @Test
    void getUserProfileWithJwt() {
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
    void checkIfEmailOrUsername() {
    }

    @Test
    void testUserLogin() {

        when(userRepository.getUserByUsername("halco1002")).thenReturn(Optional.ofNullable(userObject));

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
    void getCurrentlyLoggedUser() {
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

    @Test
    void checkIfJwtIsValid() {
    }

    @Test
    void searchUsersByName() {
    }
}