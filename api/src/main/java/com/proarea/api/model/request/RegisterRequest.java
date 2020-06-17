package com.proarea.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotNull
    private String password;

    @NotNull
    private String username;

    @NotNull
    private String name;

}
