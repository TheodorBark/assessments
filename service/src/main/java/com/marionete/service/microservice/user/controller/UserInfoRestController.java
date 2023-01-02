package com.marionete.service.microservice.user.controller;

import com.marionete.service.microservice.user.client.UserInfoService;
import com.marionete.service.microservice.user.model.UserInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
public class UserInfoRestController {

    private final UserInfoService userInfoService;

    @GetMapping("marionete/user")
    public ResponseEntity<UserInfoDto> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return ResponseEntity.ok(userInfoService.getUserInfo(token));
    }
}