package com.marionete.service.microservice.login.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.marionete.service.microservice.account.model.AccountInfoDto;
import com.marionete.service.microservice.user.model.UserInfoDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonPropertyOrder({ "accountInfo", "userInfo" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccountDto {

    @NotNull
    @JsonProperty(value = "accountInfo")
    private AccountInfoDto accountInfo;

    @NotNull
    @JsonProperty(value = "userInfo")
    private UserInfoDto useeInfo;
}