package com.marionete.service.microservice.user.controller;

import com.marionete.proto.lib.TokenRequest;
import com.marionete.service.microservice.user.client.UserInfoService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(UserInfoRestController.class)
public class UserInfoRestControllerTest {
    private static final String HEADER = "Authorization";
    private static final String TOKEN = "jwt_token";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserInfoService service;

    @Test
    public void whenGetUserInfo_thenReturnJsonUserInfo() throws Exception {

        given(service.getUserInfo(TOKEN)).willReturn(buildUserInfoDto());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1//marionete/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER, TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Theo"))
                .andDo(MockMvcResultHandlers.print());
    }

    private UserInfoDto buildUserInfoDto() {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setName("Theo");
        userInfoDto.setSurname("Barkhuizebn");
        userInfoDto.setSex("Male");
        userInfoDto.setAge(46);

        return userInfoDto;
    }
}
