package com.proarea.api.model;

import com.proarea.api.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CurrentUserDetails implements UserDetails {
    private Long userID;
    private String emailAddress;
    private String password;

    public CurrentUserDetails(Long userID, String emailAddress, String password) {
        super();
        this.userID = userID;
        this.emailAddress = emailAddress;
        this.password = password;
    }


  /*    public static UserDetails create(Users entity) {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for(Authorities auth: entity.getAuthorities()){
        authorities.add(new SimpleGrantedAuthority(auth.getId().getAuthority()));
    }
    return new MyUserDetail(entity.getUserId(), entity.getLoginId(), entity.getPassword(), entity.getDisplayName(), authorities);
}*/



    public Long getUserID(){
        return this.userID;
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    public String getEmailAddress() {
        return this.emailAddress;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetails create(UserEntity entity) {
       return new CurrentUserDetails(entity.getId(), entity.getUsername(), entity.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> "ROLE_USER");
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }
}