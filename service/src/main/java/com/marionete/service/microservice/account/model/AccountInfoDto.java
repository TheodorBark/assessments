package com.marionete.service.microservice.account.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "accountInfo")
public class AccountInfoDto {

    @NotNull
    @NotEmpty
    @JsonProperty(value = "accountNumber")
    private String accountNumber;
}