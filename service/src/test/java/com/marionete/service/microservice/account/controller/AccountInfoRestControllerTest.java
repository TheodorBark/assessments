package com.marionete.service.microservice.account.controller;

import com.marionete.service.microservice.account.client.AccountInfoService;
import com.marionete.service.microservice.account.model.AccountInfoDto;
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

import java.io.IOException;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountInfoRestController.class)
public class AccountInfoRestControllerTest {
    private static final String HEADER = "Authorization";
    private static final String TOKEN = "jwt_token";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountInfoService service;

    @Test
    public void whenGetAccountInfo_thenReturnJsonAccountInfo() throws Exception {

        for (int counter = 0; counter < 3; counter++) {
            try {
                given(service.getAccounntInfo(TOKEN)).willReturn(buildAccountInfoDto());

                mvc.perform(MockMvcRequestBuilders.get("/api/v1//marionete/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HEADER, TOKEN))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value("12345-3346-3335-4456"))
                        .andDo(MockMvcResultHandlers.print());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private AccountInfoDto buildAccountInfoDto() {
        AccountInfoDto accountInfoDto = new AccountInfoDto();
        accountInfoDto.setAccountNumber("12345-3346-3335-4456");

        return accountInfoDto;
    }
}