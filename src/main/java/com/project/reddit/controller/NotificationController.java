package com.project.reddit.controller;

import com.project.reddit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllNotificationsFromUser() {
        return new ResponseEntity<>(userService.getAllNotifications(), HttpStatus.OK);
    }

}
