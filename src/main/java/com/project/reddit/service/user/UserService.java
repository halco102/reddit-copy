package com.project.reddit.service.user;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.comment.LikedOrDislikedCommentsUser;
import com.project.reddit.dto.comment.UserProfileCommentsWithPostId;
import com.project.reddit.dto.post.PostLikeOrDislikeDto;
import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.dto.user.login.UserLoginRequestDto;
import com.project.reddit.dto.user.login.UserLoginResponse;
import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.dto.user.signup.UserSignupResponseDto;
import com.project.reddit.exception.ClassCastException;
import com.project.reddit.exception.*;
import com.project.reddit.mapper.CommentMapper;
import com.project.reddit.mapper.UserMapper;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.security.CustomUserDetailsImp;
import com.project.reddit.security.JwtTokenUtil;
import com.project.reddit.service.search.Search;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private final Search<User> userSearch;

    private final CommentMapper commentMapper;

    @Value("${MAIL_USERNAME}")
    private String email;

    @Value("${VERIFY_EMAIL_URL}")
    private String verifyEmailUrl;





    @Transactional
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
        String verifyUrl = verifyEmailUrl + "/api/v1/user/verify/" + user.getVerificationCode();
        
        String toAddress = user.getEmail();
        String fromAddress = this.email;
        String subject = "Successfully register in Reddit copy app ";
        String content = "Dear " + user.getUsername() + " thank you for signin up my project website.\n\n" +
                "Please follow this link <a href =" + verifyUrl + "> Verify here </a>"+ " <br>" +
                "I hope you like it.<br>" +
                "Best regards " + "<br>" +
                "Halco";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(fromAddress);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);



        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessage);
    }

    public String verifieUserViaEmail(String code) {
        var user = this.userRepository.verifieUserCode(code);

        if (user.isEmpty()) {
            throw new BadRequestException("User not found or code is wrong");
        }

        user.get().setVerified(true);
        userRepository.save(user.get());

        return "Verified continue to vue website http://localhost:8081/";
    }

    private UserProfileDto fetchUserProfileById(Long id) {

        var userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new NotFoundException("The user with id: " + id + " does not exist");
        }

/*        var user = userMapper.userProfileDto(userById.get());

        if (userById.get().getLikeDislikes() != null || userById.get().getLikeDislikes().isEmpty()) {
            user.setLikedOrDislikedComments(getAllLikedOrDislikedCommentsByUser(userById.get()));
        }

        if (userById.get().getPostLikeOrDislikes() != null || userById.get().getPostLikeOrDislikes().isEmpty()) {
            user.setPostLikeOrDislikeDtos(getAllPostLikeOrDislikeByUser(userById.get()));
        }

        if (userById.get().getComments() != null || userById.get().getComments().isEmpty()){
            user.setCommentsPosts(getAllCommentsWithPostId(userById.get()));
        }*/

        return castToUserProfile(userById);
    }

    private List<LikedOrDislikedCommentsUser> getAllLikedOrDislikedCommentsByUser(User user) {
        return user.getLikeDislikes().stream().map(e -> userMapper.likeOrDislike(e)).collect(Collectors.toList());
    }

    private List<PostLikeOrDislikeDto> getAllPostLikeOrDislikeByUser(User user) {
        return user.getPostLikeOrDislikes().stream().map(e -> userMapper.postLikeOrDislike(e)).collect(Collectors.toList());
    }

    private List<UserProfileCommentsWithPostId> getAllCommentsWithPostId(User user) {
        var commentWithPostId = new ArrayList<UserProfileCommentsWithPostId>();

        Multimap<Long, CommentDto> temp = MultimapBuilder.treeKeys().arrayListValues().build();

        user.getComments().stream()
                .forEach(e -> {
                    temp.put(e.getPost().getId(), commentMapper.toDto(e));
                });


        for (var m : temp.asMap().entrySet()) {
            commentWithPostId.add(new UserProfileCommentsWithPostId(m.getKey(), m.getValue().stream().collect(Collectors.toSet())));
        }

        return commentWithPostId;
    }

    private UserProfileDto castToUserProfile(Optional<User> user) {

        var userDto = userMapper.userProfileDto(user.get());


        if (user.get().getLikeDislikes() != null || user.get().getLikeDislikes().isEmpty()) {
            userDto.setLikedOrDislikedComments(getAllLikedOrDislikedCommentsByUser(user.get()));
        }

        if (user.get().getPostLikeOrDislikes() != null || user.get().getPostLikeOrDislikes().isEmpty()) {
            userDto.setPostLikeOrDislikeDtos(getAllPostLikeOrDislikeByUser(user.get()));
        }

        if (user.get().getComments() != null || user.get().getComments().isEmpty()){
            userDto.setCommentsPosts(getAllCommentsWithPostId(user.get()));
        }

        return userDto;
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

/*        var user = userMapper.userProfileDto(userByUsername.get());

        if (userByUsername.get().getLikeDislikes() != null || userByUsername.get().getLikeDislikes().isEmpty()) {
            user.setLikedOrDislikedComments(getAllLikedOrDislikedCommentsByUser(userByUsername.get()));
        }

        if (userByUsername.get().getPostLikeOrDislikes() != null || userByUsername.get().getPostLikeOrDislikes().isEmpty()) {
            user.setPostLikeOrDislikeDtos(getAllPostLikeOrDislikeByUser(userByUsername.get()));
        }

        if (userByUsername.get().getComments() != null || userByUsername.get().getComments().isEmpty()){
            user.setCommentsPosts(getAllCommentsWithPostId(userByUsername.get()));
        }*/

        return castToUserProfile(userByUsername);
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

        return castToUserProfile(Optional.of(user));
    }

    public User getUserById(Long id) {

        if (id == null) {
            throw new NotFoundException("Id is null");
        }
        var user = this.userRepository.findById(id);
        return user.orElseThrow(() -> {throw new NotFoundException("The user with id: " + id + " does not exist");});
    }

    /*
    * true = email
    * false = username
    * */
    public boolean checkIfEmailOrUsername(String emailOrUsername) {
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher matcher = pattern.matcher(emailOrUsername);

        return matcher.matches();
    }



    public UserLoginResponse userLogin(UserLoginRequestDto requestDto) {

        Optional<User> getUser = null;

        if (checkIfEmailOrUsername(requestDto.getEmailOrUsername())) {
            getUser = this.userRepository.getUserByEmail(requestDto.getEmailOrUsername().trim());
        }else {
            getUser = this.userRepository.getUserByUsername(requestDto.getEmailOrUsername());
        }

        if(getUser.isEmpty()){
            throw new NotFoundException("The user with email or username: " + requestDto.getEmailOrUsername().trim() + " does not exist");
        }

        if (passwordEncoder.matches(requestDto.getPassword(), getUser.get().getPassword())) {
            log.info("Password match");

            if (!getUser.get().isVerified()){
                throw new BadRequestException("Verify email to login");
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(getUser.get().getUsername(), requestDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            var mapper = userMapper.userLoginResponseDto(jwtTokenUtil.generateJwtToken(auth));


            return mapper;

        }else {
            throw new Unauthorized("The password does not match");
        }

    }

    public User getCurrentlyLoggedUser() {

        User user = null;
        String username = null;

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

    public UserProfileDto getAllPostsByUser(Long id) {
        var getAllPostsByUser = this.userRepository.getAllPostByUserId(id);
        if (getAllPostsByUser.isEmpty()) {
            throw new NotFoundException("The object is null");
        }
        var temp = userMapper.userProfileDto(getAllPostsByUser.get());
        return temp;
    }

    public boolean checkIfJwtIsValid(String jwt) {
        return this.jwtTokenUtil.validateToken(jwt);
    }

    public Set<UserProfileDto> searchUsersByName(String name) {
        var searchResults = this.userSearch.search(name);

        return searchResults.stream().map(m -> userMapper.userProfileDto(m)).collect(Collectors.toSet());
    }

/*    private UserProfileCommentsWithPostObject setCommentsWithPostReference(User user) {

        Multimap<Post, Comment> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        //get all user posts
        user.getPosts()
                //for each
                .stream()
                .forEach(element -> {
                    // find comments from specific user and add the post object

                });
    }*/


}
