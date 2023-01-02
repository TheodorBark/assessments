package com.marionete.service.microservice.account.server;

import com.marionete.proto.lib.AccountInfoResponse;
import com.marionete.proto.lib.AccountInfoServiceGrpc;
import com.marionete.proto.lib.TokenRequest;
import com.marionete.service.microservice.connector.RestConnector;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@GrpcService
public class AccountInfoServiceImpl extends AccountInfoServiceGrpc.AccountInfoServiceImplBase {
    private final RestConnector restConnector;

    @Override
    public void getAccountInfo(TokenRequest request, StreamObserver<AccountInfoResponse> responseObserver) {
        var response = restConnector.getAccountInfo(request.getToken());
        var accountInfoResponse = AccountInfoResponse.newBuilder()
                .setAccountNumber(response.getAccountNumber())
                .build();

        responseObserver.onNext(accountInfoResponse);
        responseObserver.onCompleted();
    }
}
