package com.marionete.service.microservice.login.client;

import com.marionete.proto.lib.LoginRequest;
import com.marionete.proto.lib.LoginServiceGrpc;
import com.marionete.proto.lib.TokenRequest;
import com.marionete.service.microservice.account.model.AccountInfoDto;
import com.marionete.service.microservice.login.model.LoginRequestDto;
import com.marionete.service.microservice.login.model.UserAccountDto;
import com.marionete.service.microservice.user.model.UserInfoDto;
import io.github.majusko.grpc.jwt.data.GrpcHeader;
import io.github.majusko.grpc.jwt.interceptor.AuthClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
public class LoginService {
    @Value("${grpc.server.name:localhost}")
    private String serverName;
    @Value("${grpc.server.port-number:9090}")
    private int portNumber;

    private final AuthClientInterceptor authClientInterceptor;

    public UserAccountDto login(LoginRequestDto loginRequest)  {
        var channel = ManagedChannelBuilder.forAddress(serverName, portNumber)
                .usePlaintext()
                .build();
        var interceptedChannel = ClientInterceptors.intercept(channel, authClientInterceptor);
        var loginBlockingStubStub = LoginServiceGrpc.newBlockingStub(interceptedChannel);

        var request = LoginRequest.newBuilder()
                .setUsername(loginRequest.getUserName())
                .setPassword(loginRequest.getPassword())
                .build();

        var response = loginBlockingStubStub.login(request);

        var header = new Metadata();
        header.put(GrpcHeader.AUTHORIZATION, response.getToken());

        var headerChannel = ClientInterceptors.intercept(NettyChannelBuilder.forTarget(serverName + ":" + portNumber)
                        .usePlaintext()
                        .build(),
                        MetadataUtils.newAttachHeadersInterceptor(header));

        var userAccountBlockingStub = LoginServiceGrpc.newBlockingStub(headerChannel);
        var tokenRequest = TokenRequest.newBuilder().setToken(response.getToken()).build();

        var userAccount = userAccountBlockingStub.getUserAccount(tokenRequest);

        channel.shutdown();

        var userAccountInfoDto = new UserAccountDto();

        var accountInfoDto = new AccountInfoDto();
        accountInfoDto.setAccountNumber(userAccount.getAccountInfo().getAccountNumber());
        userAccountInfoDto.setAccountInfo(accountInfoDto);

        var userInfoDto = new UserInfoDto();
        userInfoDto.setName(userAccount.getUserInfo().getName());
        userInfoDto.setSurname(userAccount.getUserInfo().getSurname());
        userInfoDto.setSex(userAccount.getUserInfo().getSex());
        userInfoDto.setAge(userAccount.getUserInfo().getAge());

        userAccountInfoDto.setUseeInfo(userInfoDto);

        return userAccountInfoDto;
    }
}