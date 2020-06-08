package com.proarea.api.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {

    static private List<SimpleGrantedAuthority> authorities;

    static {
        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    private String id;
    private String email;
    private String name;

    public static List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

}
