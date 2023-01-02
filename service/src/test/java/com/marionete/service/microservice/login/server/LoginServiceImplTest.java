package com.marionete.service.microservice.login.server;

import com.marionete.proto.lib.LoginRequest;
import com.marionete.proto.lib.LoginResponse;
import com.marionete.service.microservice.connector.RestConnector;
import io.github.majusko.grpc.jwt.GrpcJwtProperties;
import io.github.majusko.grpc.jwt.service.JwtService;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.StandardEnvironment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LoginServiceImplTest {
    private LoginServiceImpl loginService;

    @Before
    public void setup() {
        StandardEnvironment environment = new StandardEnvironment();
        GrpcJwtProperties grpcJwtProperties = new GrpcJwtProperties();
        JwtService jwtService = new JwtService(environment, grpcJwtProperties);
        RestConnector restConnector = new RestConnector();
        loginService = new LoginServiceImpl(jwtService, restConnector);
    }

    @Test
    public void whenLogin_thenReturnLoginResponset() throws Exception {
        StreamRecorder<LoginResponse> responseObserver = StreamRecorder.create();
        loginService.login(buildLoginRequest(), responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }

        assertNull(responseObserver.getError());
        List<LoginResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());

        LoginResponse response = results.get(0);
        assertNotNull(response);
    }

    private LoginRequest buildLoginRequest() {
        return LoginRequest.newBuilder()
                .setUsername("Test")
                .setPassword("Test")
                .build();
    }
}