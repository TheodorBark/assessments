package com.marionete.service.microservice.login.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonPropertyOrder({ "username", "password" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequestDto {

    @NotNull
    @NotEmpty
    @JsonProperty(value = "username")
    private String userName;

    @NotNull
    @NotEmpty
    @JsonProperty(value = "password")
    private String password;
}