package com.project.reddit.controller;

import com.project.reddit.dto.user.login.UserLoginRequestDto;
import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.service.user.UserService;
import com.project.reddit.service.user.follow.FollowService;
import com.project.reddit.service.user.follow.IFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final IFollow followInterface;


    @PostMapping()
    public ResponseEntity<?> signupUser(@RequestBody @Valid UserSignupRequestDto signupRequestDto) {
        return new ResponseEntity<>(this.userService.signupUser(signupRequestDto), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getUserByUsernameOrId(@RequestParam(required = false) String username, @RequestParam(required = false) Long id) {
        return new ResponseEntity<>(this.userService.getUserProfileByUsernameOrId(username,id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginRequestDto dto) {
        return new ResponseEntity<>(this.userService.userLogin(dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfileWithJwt() {
        return new ResponseEntity<>(this.userService.getUserProfileWithJwt(), HttpStatus.OK);
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<?> verifyUserCode(@PathVariable("code") String code) {
        return new ResponseEntity<>(this.userService.verifieUserViaEmail(code), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<?> getAllPostsFromUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.userService.getAllPostsByUser(id), HttpStatus.OK);
    }

    @GetMapping("/jwt/valid")
    public ResponseEntity<?> checkIfJwtIsValid(@RequestParam String jwt) {
        return new ResponseEntity<>(this.userService.checkIfJwtIsValid(jwt), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/follow/{id}")
    public ResponseEntity<?> followUserById(@PathVariable Long id) {
        return new ResponseEntity<>(followInterface.followUser(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/unfollow/{id}")
    public ResponseEntity<?> unfollowUserById(@PathVariable Long id) {
        return new ResponseEntity<>(followInterface.unfollowUser(id), HttpStatus.OK);
    }
}
