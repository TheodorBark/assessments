package com.marionete.service.microservice.exception.grpc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetails {
    public enum Code {
        RESOURCE_ID("err_resource_id"),
        MESSAGE("err_message");

        private final String key;

        Code(String key) {
            this.key = key;
        }
    }
}