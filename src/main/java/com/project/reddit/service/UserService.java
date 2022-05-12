package com.project.reddit.service;

import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.dto.user.login.UserLoginRequestDto;
import com.project.reddit.dto.user.login.UserLoginResponse;
import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.dto.user.signup.UserSignupResponseDto;
import com.project.reddit.exception.ClassCastException;
import com.project.reddit.exception.*;
import com.project.reddit.mapper.UserMapper;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.security.CustomUserDetailsImp;
import com.project.reddit.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final String RANDOM_AVATAR_URL="https://avatars.dicebear.com/api/bottts/";

    private final JavaMailSender mailSender;


    public UserSignupResponseDto signupUser(UserSignupRequestDto signupRequest) {

        var user = userMapper.signupToEntity(signupRequest);
        user.setCreatedAt(LocalDate.now());
        user.setImageUrl(RANDOM_AVATAR_URL + UUID.randomUUID() + ".svg");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.ROLE_USER);
        user.setVerificationCode(String.valueOf(UUID.randomUUID()));

        if(userRepository.getUserByUsername(signupRequest.getUsername()).isPresent()) {
            throw new DuplicateException("Username is already taken");
        }else if (userRepository.getUserByEmail(signupRequest.getEmail()).isPresent()) {
            throw new DuplicateException("Email is already taken");
        }

        var saveEntity = userRepository.save(user);
        log.info("User successfully saved to db");

        try {
            sendVerificationEmail(user);
        }catch (MessagingException ex) {
            log.warn(ex.getMessage());
        }

        return userMapper.signupResponseDto(saveEntity);
    }

    private void sendVerificationEmail(User user) throws MessagingException {
        String toAddress = user.getEmail();
        String fromAddress = "Halco";
        String subject = "Successfully register in Reddit copy app ";
        String content = "Dear + " + user.getUsername() + " thank you for signin up my project website. I hope you like it. \n Best regards Halco";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(fromAddress);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);

        String verifyUrl = "/api/v1/user/verify/" + user.getVerificationCode();

        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessage);
    }

    private UserProfileDto fetchUserProfileById(Long id) {
        var userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new NotFoundException("The user with id: " + id + " does not exist");
        }
        var user = userMapper.userProfileDto(userById.get());

        if (userById.get().getLikeDislikes() != null || userById.get().getLikeDislikes().isEmpty()) {
            var like = userById.get().getLikeDislikes().stream().map(e -> userMapper.likeOrDislike(e)).collect(Collectors.toList());
            user.setLikedOrDislikedComments(like);
        }

        return user;
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

    public UserProfileDto getUserProfileWithJwt() {
        var user = getCurrentlyLoggedUser();

        return userMapper.userProfileDto(user);
    }

    public User getUserById(Long id) {

        if (id == null) {
            throw new NotFoundException("Id is null");
        }
        var user = this.userRepository.findById(id);
        return user.orElseThrow(() -> {throw new NotFoundException("The user with id: " + id + " does not exist");});
    }

    public UserLoginResponse userLogin(UserLoginRequestDto requestDto) {

        var getUserByEmail = this.userRepository.getUserByEmail(requestDto.getEmail().trim());

        if(getUserByEmail.isEmpty()){
            throw new NotFoundException("The user with email: " + requestDto.getEmail().trim() + " does not exist");
        }

        if (passwordEncoder.matches(requestDto.getPassword(), getUserByEmail.get().getPassword())) {
            log.info("Password match");
            UserLoginResponse userLoginResponse = new UserLoginResponse();
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(getUserByEmail.get().getUsername(), requestDto.getPassword())
            );
                    //new UsernamePasswordAuthenticationToken(getUserByEmail.get().getUsername(), passwordEncoder.encode(getUserByEmail.get().getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);

            var mapper = userMapper.userLoginResponseDto(getUserByEmail.get(), jwtTokenUtil.generateJwtToken(auth));


            return mapper;

        }else {
            throw new Unauthorized("The password does not match");
        }

    }

    protected User getCurrentlyLoggedUser() {
        User user = null;
        String username = null;

        var temp = SecurityContextHolder.getContext();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            throw new Unauthorized("Not authorized");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetailsImp) {
            username = ((CustomUserDetailsImp) principal).getUsername();
            return this.userRepository.getUserByUsername(username).orElseThrow(() -> new NotFoundException("User not found!"));
        }else {
            throw new ClassCastException("Class cast exception");
        }

    }


}
