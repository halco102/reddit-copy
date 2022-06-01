package com.project.reddit.controller;

import com.project.reddit.dto.user.login.UserLoginRequestDto;
import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.service.PostService;
import com.project.reddit.service.UserService;
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

}
