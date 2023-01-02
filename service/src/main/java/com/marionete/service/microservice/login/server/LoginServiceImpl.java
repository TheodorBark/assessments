package com.marionete.service.microservice.login.server;

import com.marionete.proto.lib.*;
import com.marionete.service.microservice.connector.RestConnector;
import io.github.majusko.grpc.jwt.annotation.Allow;
import io.github.majusko.grpc.jwt.service.GrpcRole;
import io.github.majusko.grpc.jwt.service.JwtService;
import io.github.majusko.grpc.jwt.service.dto.JwtData;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@RequiredArgsConstructor
@Component
@GrpcService
public class LoginServiceImpl extends LoginServiceGrpc.LoginServiceImplBase {
    private final JwtService jwtService;
    private final RestConnector restConnector;

    @Allow(roles = GrpcRole.INTERNAL)
    @Override
    public void login(LoginRequest loginRequest, StreamObserver<LoginResponse> responseObserver) {
        var response = LoginResponse.newBuilder()
                .setToken(generateToken(loginRequest.getUsername()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Allow(roles = GrpcRole.INTERNAL)
    @Override
    public void getUserAccount(TokenRequest request, StreamObserver<UserAccountResponse> responseObserver) {
        var response = restConnector.getUserAccountInfo(request.getToken());

        var accountInfoResponse = AccountInfoResponse.newBuilder()
                .setAccountNumber(response.getAccountInfo().getAccountNumber())
                .build();

        var userInoResponse = UserInfoResponse.newBuilder()
                .setName(response.getUseeInfo().getName())
                .setSurname(response.getUseeInfo().getSurname())
                .setSex(response.getUseeInfo().getSex())
                .setAge(response.getUseeInfo().getAge())
                .build();

        var userAccountResponse = UserAccountResponse.newBuilder()
                .setAccountInfo(accountInfoResponse)
                .setUserInfo(userInoResponse)
                .build();

        responseObserver.onNext(userAccountResponse);
        responseObserver.onCompleted();
    }

    private String generateToken(String userName) {
        var data = new JwtData(userName, new HashSet<>());
        return jwtService.generate(data);
    }
}