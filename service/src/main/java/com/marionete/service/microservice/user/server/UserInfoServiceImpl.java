package com.marionete.service.microservice.user.server;

import com.marionete.proto.lib.TokenRequest;
import com.marionete.proto.lib.UserInfoResponse;
import com.marionete.proto.lib.UserInfoServiceGrpc;
import com.marionete.service.microservice.connector.RestConnector;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@GrpcService
public class UserInfoServiceImpl extends UserInfoServiceGrpc.UserInfoServiceImplBase {
    private final RestConnector restConnector;

    @Override
    public void getUserInfo(TokenRequest request, StreamObserver<UserInfoResponse> responseObserver) {

        var response = restConnector.getUserInfo(request.getToken());

        var userInfoResponse = UserInfoResponse.newBuilder()
                .setName(response.getName())
                .setSurname(response.getSurname())
                .setSex(response.getSex())
                .setAge(response.getAge())
                .build();

        responseObserver.onNext(userInfoResponse);
        responseObserver.onCompleted();
    }
}