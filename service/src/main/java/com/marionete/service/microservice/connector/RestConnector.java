package com.marionete.service.microservice.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marionete.backends.AccountInfoMock;
import com.marionete.backends.UserInfoMock;
import com.marionete.service.microservice.account.model.AccountInfoDto;
import com.marionete.service.microservice.login.model.UserAccountDto;
import com.marionete.service.microservice.user.model.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class RestConnector {

    private static final String AUTH_HEADER = "Authorization";
    private static final String ACCOUNNT_INFO_URI = "http://localhost:8899/marionete/account/";
    private static final String USER_INFO_URI = "http://localhost:8898/marionete/user/";
    private static final int RETRY_COUNT = 3;

    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public AccountInfoDto getAccountInfo(String token) {
        AccountInfoDto dto = null;
        var server = AccountInfoMock.start();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ACCOUNNT_INFO_URI))
                .header(AUTH_HEADER, token)
                .build();

        for (int counter = 0; counter < RETRY_COUNT; counter++) {
            try {
                var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
                dto = new ObjectMapper().readValue(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS), AccountInfoDto.class);
            } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex) {
                log.error(Arrays.toString(ex.getStackTrace()));
            }
        }

        server.close();
        return dto;
    }

    public UserInfoDto getUserInfo(String token) {
        UserInfoDto dto = null;
        var server = UserInfoMock.start();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(USER_INFO_URI))
                    .header(AUTH_HEADER, token)
                    .build();

            var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            dto = new ObjectMapper().readValue(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS), UserInfoDto.class);
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex)  {
            log.error(Arrays.toString(ex.getStackTrace()));
        } finally {
            server.close();
        }

        return dto;
    }

    public UserAccountDto getUserAccountInfo(String token) {
        UserAccountDto dto = new UserAccountDto();
        var accountServer = AccountInfoMock.start();
        var userServer = UserInfoMock.start();

        HttpRequest accoutRequest = HttpRequest.newBuilder()
                .uri(URI.create(ACCOUNNT_INFO_URI))
                .header(AUTH_HEADER, token)
                .build();

        HttpRequest userRequest = HttpRequest.newBuilder()
                .uri(URI.create(USER_INFO_URI))
                .header(AUTH_HEADER, token)
                .build();

        for (int counter = 0; counter < RETRY_COUNT; counter++) {
            try {
                var accountResponse = client.send(accoutRequest, HttpResponse.BodyHandlers.ofString());
                AccountInfoDto accountInfoDto = new ObjectMapper().readValue(accountResponse.body(), AccountInfoDto.class);
                var userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
                UserInfoDto userInfoDto = new ObjectMapper().readValue(userResponse.body(), UserInfoDto.class);

                dto.setAccountInfo(accountInfoDto);
                dto.setUseeInfo(userInfoDto);
            } catch (IOException | InterruptedException ex) {
                log.error(Arrays.toString(ex.getStackTrace()));
            }
        }

        accountServer.close();
        userServer.close();

        return dto;
    }
}