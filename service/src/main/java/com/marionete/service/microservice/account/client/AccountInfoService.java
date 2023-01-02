package com.marionete.service.microservice.account.client;

import com.marionete.proto.lib.AccountInfoServiceGrpc;
import com.marionete.proto.lib.TokenRequest;
import com.marionete.service.microservice.account.model.AccountInfoDto;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AccountInfoService {
    @Value("${grpc.server.name:localhost}")
    private String serverName;
    @Value("${grpc.server.port-number:9090}")
    private int portNumber;

    public AccountInfoDto getAccounntInfo(String token) {
        var channel = ManagedChannelBuilder.forAddress(serverName, portNumber)
                .usePlaintext()
                .build();

        var accountInfoBlockingStub = AccountInfoServiceGrpc.newBlockingStub(channel);
        var request = TokenRequest.newBuilder().setToken(token).build();
        var response = accountInfoBlockingStub.getAccountInfo(request);

        var accountInfoDto = new AccountInfoDto();
        accountInfoDto.setAccountNumber(response.getAccountNumber());

        return accountInfoDto;
    }
}
