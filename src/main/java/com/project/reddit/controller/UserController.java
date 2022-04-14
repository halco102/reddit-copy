package com.project.reddit.controller;

import com.project.reddit.dto.user.UserLoginRequestDto;
import com.project.reddit.dto.user.UserSignupRequestDto;
import com.project.reddit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("")
    public ResponseEntity<?> getUserByUsernameOrId(@RequestParam(required = false) String username, @RequestParam(required = false) Long id) {
        return new ResponseEntity<>(this.userService.getUserProfileByUsernameOrId(username,id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginRequestDto dto) {
        return new ResponseEntity<>(this.userService.userLogin(dto), HttpStatus.OK);
    }

}
