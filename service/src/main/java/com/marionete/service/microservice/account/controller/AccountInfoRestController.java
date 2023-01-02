package com.marionete.service.microservice.account.controller;

import com.marionete.service.microservice.account.client.AccountInfoService;
import com.marionete.service.microservice.account.model.AccountInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
public class AccountInfoRestController {

    private final AccountInfoService accountInfoService;

    @GetMapping("marionete/account")
    public ResponseEntity<AccountInfoDto> getAccountInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return ResponseEntity.ok(accountInfoService.getAccounntInfo(token));
    }
}