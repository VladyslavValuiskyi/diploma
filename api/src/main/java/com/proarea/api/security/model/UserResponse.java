package com.proarea.api.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private Long id;

    @JsonIgnore
    private boolean enabled;

    private String email;

    private String name;

    private boolean hasNotification;

}
