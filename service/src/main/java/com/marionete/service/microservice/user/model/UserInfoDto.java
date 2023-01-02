package com.marionete.service.microservice.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonPropertyOrder({ "name", "surnname", "sex", "age" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "userInfo")
public class UserInfoDto {

    @NotNull
    @NotEmpty
    @JsonProperty(value = "name")
    private String name;

    @NotNull
    @NotEmpty
    @JsonProperty(value = "surname")
    private String surname;

    @NotNull
    @NotEmpty
    @JsonProperty(value = "sex")
    private String sex;

    @JsonProperty(value = "age")
    private int age;
}