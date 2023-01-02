package com.marionete.service.microservice.user.client;

import com.marionete.proto.lib.TokenRequest;
import com.marionete.proto.lib.UserInfoServiceGrpc;
import com.marionete.service.microservice.user.model.UserInfoDto;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserInfoService {
    @Value("${grpc.server.name:localhost}")
    private String serverName;
    @Value("${grpc.server.port-number:9090}")
    private int portNumber;

    public UserInfoDto getUserInfo(String token) {
        var channel = ManagedChannelBuilder.forAddress(serverName, portNumber)
                .usePlaintext()
                .build();

        var userInfoBlockingStub = UserInfoServiceGrpc.newBlockingStub(channel);
        var request = TokenRequest.newBuilder().setToken(token).build();

        var response = userInfoBlockingStub.getUserInfo(request);

        var userInfoDto = new UserInfoDto();
        userInfoDto.setName(response.getName());
        userInfoDto.setSurname(response.getSurname());
        userInfoDto.setSex(response.getSex());
        userInfoDto.setAge(response.getAge());

        return userInfoDto;
    }
}