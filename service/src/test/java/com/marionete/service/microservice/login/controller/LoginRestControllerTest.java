package com.marionete.service.microservice.login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marionete.service.microservice.account.model.AccountInfoDto;
import com.marionete.service.microservice.login.client.LoginService;
import com.marionete.service.microservice.login.model.LoginRequestDto;
import com.marionete.service.microservice.login.model.UserAccountDto;
import com.marionete.service.microservice.user.model.UserInfoDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginRestController.class)
public class LoginRestControllerTest {
    private static final String HEADER = "Authorization";
    private static final String TOKEN = "jwt_token";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoginService service;

    @Test
    public void whenLogin_thenReturnJsonUserAccount() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUserName("TestUsernamw");
        loginRequestDto.setPassword("TestPassword");

        try {
            given(service.login(loginRequestDto)).willReturn(buildUserAccount());

            mvc.perform(MockMvcRequestBuilders
                            .post("/api/v1//marionete/useraccount")
                            .header(HEADER, TOKEN)
                            .content(asJsonString(loginRequestDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private UserAccountDto buildUserAccount() {
        AccountInfoDto accountInfoDto = new AccountInfoDto();
        accountInfoDto.setAccountNumber("12345-3346-3335-4456");

        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setName("Theo");
        userInfoDto.setSurname("Barkhuizen");
        userInfoDto.setSex("Male");
        userInfoDto.setAge(46);

        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setAccountInfo(accountInfoDto);
        userAccountDto.setUseeInfo(userInfoDto);

        return userAccountDto;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}