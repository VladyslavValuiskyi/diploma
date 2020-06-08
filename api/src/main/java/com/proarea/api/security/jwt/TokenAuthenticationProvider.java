package com.proarea.api.security.jwt;

import com.proarea.api.model.entity.UserEntity;
import com.proarea.api.repository.UserRepository;
import com.proarea.api.security.web.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

@Slf4j
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenBuilder tokenBuilder;
    private final UserRepository userRepository;

    public TokenAuthenticationProvider(JwtTokenBuilder tokenBuilder, UserRepository userRepository) {
        this.tokenBuilder = tokenBuilder;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.trace("[TokenAuthenticationProvider] [authenticate]");
        String rawAccessToken = (String) authentication.getCredentials();
        if (rawAccessToken == null) {
            throw new BadCredentialsException(Messages.TOKEN_SHOULD_BE_PROVIDED);
        }
        User user = tokenBuilder.decodeToken(rawAccessToken);
        UserEntity userEntity = userRepository.findFirstByUsername(user.getUsername());
        if (userEntity == null) {
            throw new BadCredentialsException(Messages.TOKEN_SHOULD_BE_PROVIDED);
        }
        return new AuthenticationToken(userEntity, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
