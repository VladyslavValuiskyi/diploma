package com.proarea.api.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @JsonProperty("email")
    @ApiModelProperty(value = "email", required = true, example = "user@user.user")
    private String username;

    @ApiModelProperty(value = "password", required = true, example = "user")
    private String password;

}
