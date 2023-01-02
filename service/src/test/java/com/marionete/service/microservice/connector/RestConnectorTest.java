package com.marionete.service.microservice.connector;

import com.marionete.backends.AccountInfoMock;
import com.marionete.backends.UserInfoMock;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class RestConnectorTest {
    private static final String HEADER = "Authorization";
    private static final String TOKEN = "jet_token";
    private static final String ACCOUNNT_INFO_URI = "http://localhost:8899/marionete/account/";
    private static final String USER_INFO_URI = "http://localhost:8898/marionete/user/";
    private HttpClient httpClient;

    @Test
    public void testGetAccountInfoAsync() {
        var accountServer = AccountInfoMock.start();

        httpClient = createHtpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ACCOUNNT_INFO_URI))
                .header(HEADER, TOKEN)
                .build();

        for (int count = 0; count < 3; count++) {
            try {
                var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

                response.thenApply(HttpResponse::body)
                        .thenAccept(body ->
                                System.out.println("testGetAccountInfoAsync() - Received response body [" + body.substring(0, 80) + "...]."))
                        .join();

                assertThat(response.get().statusCode()).isBetween(200, 299);
                assertThat(response.get().body()).isNotEmpty();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        accountServer.close();
    }

    @Test
    public void testUserInfoAsync() throws ExecutionException, InterruptedException {
        var userServer = UserInfoMock.start();
        httpClient = createHtpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USER_INFO_URI))
                .header(HEADER, TOKEN)
                .build();

        var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        response.thenApply(HttpResponse::body)
                .thenAccept(body ->
                        System.out.println("testGetUserInfoInfoAsync() - Received response body [" + body.substring(0, 80) + "...]."))
                .join();

        userServer.close();

        assertThat(response.get().statusCode()).isBetween(200, 299);
        assertThat(response.get().body()).isNotEmpty();
    }

    private HttpClient createHtpClient() {

        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }
}