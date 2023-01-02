package com.marionete.service.microservice.exception.grpc;

import com.google.rpc.Code;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@Slf4j
@GrpcAdvice
public class GlobalGrpcExceptionHandler {

    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public StatusRuntimeException handleResourceNotFoundException(ResourceNotFoundException cause) {

        log.error("Error, message {}", cause.getMessage());

        var status =
                com.google.rpc.Status.newBuilder()
                        .setCode(Code.NOT_FOUND.getNumber())
                        .setMessage("Resource not found")
                        .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler
    public Status handleInvalidArgument(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withDescription("Your description").withCause(e);
    }
}