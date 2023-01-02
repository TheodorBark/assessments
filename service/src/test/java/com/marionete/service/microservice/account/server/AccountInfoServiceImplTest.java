package com.marionete.service.microservice.account.server;

import com.marionete.proto.lib.AccountInfoResponse;
import com.marionete.proto.lib.TokenRequest;
import com.marionete.service.microservice.account.server.AccountInfoServiceImpl;
import com.marionete.service.microservice.connector.RestConnector;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class AccountInfoServiceImplTest {
    private static final String HEADER = "Authorization";
    private static final String TOKEN = "jwt_token";

    private AccountInfoServiceImpl accountInfoService;

    @Before
    public void setup() {
        RestConnector restConnector = new RestConnector();
        accountInfoService = new AccountInfoServiceImpl(restConnector);
    }

    @Test
    public void whenLoggedIn_thenReturnAccountInfoResponset() throws Exception {
        TokenRequest request = TokenRequest.newBuilder()
                .setToken(TOKEN)
                .build();

        for (int counter = 0; counter < 3; counter++) {
            try {
                StreamRecorder<AccountInfoResponse> responseObserver = StreamRecorder.create();
                accountInfoService.getAccountInfo(request, responseObserver);

                if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
                    fail("The call did not terminate in time");
                }

                assertNull(responseObserver.getError());
                List<AccountInfoResponse> results = responseObserver.getValues();
                assertEquals(1, results.size());

                AccountInfoResponse response = results.get(0);
                assertNotNull(response);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}