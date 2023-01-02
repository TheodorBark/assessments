package com.marionete.service.microservice.user.server;

import com.marionete.proto.lib.TokenRequest;
import com.marionete.proto.lib.UserInfoResponse;
import com.marionete.service.microservice.connector.RestConnector;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserInfoImplTest {
    private static final String TOKEN = "jwt_token";

    private UserInfoServiceImpl userInfoService;

    @Before
    public void setup() {
        RestConnector restConnector = new RestConnector();
        userInfoService = new UserInfoServiceImpl(restConnector);
    }

    @Test
    public void whenLoggedIn_thenReturnUserInfoResponset() throws Exception {
        TokenRequest request = TokenRequest.newBuilder()
                .setToken(TOKEN)
                .build();

        StreamRecorder<UserInfoResponse> responseObserver = StreamRecorder.create();
        userInfoService.getUserInfo(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }

        assertNull(responseObserver.getError());

        List<UserInfoResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());

        UserInfoResponse response = results.get(0);
        assertNotNull(response);
    }
}
