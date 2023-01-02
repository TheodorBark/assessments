package com.marionete.service.microservice.login.controller;

import com.marionete.service.microservice.login.client.LoginService;
import com.marionete.service.microservice.login.model.LoginRequestDto;
import com.marionete.service.microservice.login.model.UserAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
public class LoginRestController {

    private final LoginService loginService;

    @PostMapping("marionete/useraccount")
    public ResponseEntity<UserAccountDto> login(@RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }
}