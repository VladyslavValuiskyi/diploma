package com.proarea.api.service;

import com.proarea.api.model.CurrentUserDetails;
import com.proarea.api.model.entity.UserEntity;
import com.proarea.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ( username == null || username.isEmpty() ){
            throw new UsernameNotFoundException("username is empty");
        }

        UserEntity foundUser = userRepository.findFirstByUsername(username);
        if( foundUser != null ){
            System.out.println("FOUND");
            return CurrentUserDetails.create(foundUser);

        }
        throw new UsernameNotFoundException( username + "is not found");
    }
}
