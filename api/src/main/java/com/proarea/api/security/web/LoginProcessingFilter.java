package com.proarea.api.security.web;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proarea.api.model.entity.UserEntity;
import com.proarea.api.repository.AuthoritiesRepository;
import com.proarea.api.repository.UserRepository;
import com.proarea.api.security.jwt.JwtTokenBuilder;
import com.proarea.api.security.model.LoginRequest;
import com.proarea.api.security.model.TokenDTO;
import com.proarea.api.security.model.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class LoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    private final JwtTokenBuilder tokenBuilder;

    private final WebAuthenticationUnsuccessfulHandler unsuccessfulHandler;

    private final UserRepository userRepository;

    private final AuthoritiesRepository authoritiesRepository;

    public LoginProcessingFilter(String url, ObjectMapper objectMapper, JwtTokenBuilder tokenBuilder,
                                 WebAuthenticationUnsuccessfulHandler unsuccessfulHandler, UserRepository userRepository, AuthoritiesRepository authoritiesRepository) {
        super(url);
        log.trace("[LoginProcessingFilter] url={}", url);
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.objectMapper = objectMapper;
        this.tokenBuilder = tokenBuilder;
        this.unsuccessfulHandler = unsuccessfulHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.trace("[LoginProcessingFilter] [attemptAuthentication].");
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            log.debug("Authentication method  '{}' not supported.", request.getMethod());
            throw new AuthenticationServiceException(Messages.AUTHENTICATION_METHOD_NOT_SUPPORTED);
        }
        LoginRequest loginRequest;
        try {
            loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
            loginRequest.setUsername(loginRequest.getUsername().toLowerCase());
            log.info("[LoginProcessingFilter] User login '{}:{}'", loginRequest.getUsername(), loginRequest.getPassword());
        } catch (JsonMappingException e) {
            throw new AuthenticationServiceException(Messages.NO_CREDENTIALS);
        }
        if (StringUtils.isEmpty(loginRequest.getUsername()) || StringUtils.isEmpty(loginRequest.getPassword())) {
            throw new AuthenticationServiceException(Messages.NO_CREDENTIALS);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), Collections.singleton((GrantedAuthority) () -> "ROLE_USER"));
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = null;

        try {
            authentication = this.getAuthenticationManager().authenticate(authToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }

        UserResponse userResponse = buildUserResponse(userRepository.findFirstByUsername(loginRequest.getUsername()));

        if (userResponse != null) {
            if (!userResponse.isEnabled()) {
                throw new AuthenticationServiceException(Messages.USER_WAS_DELETED);
            }
        }

        User user = (User) authentication.getPrincipal();
        String token = tokenBuilder.encodeToken(userResponse, user);
        return new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        log.info("[LoginProcessingFilter] New token has been issued to user '{}'.", authentication.getPrincipal());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new TokenDTO(authentication.getCredentials().toString()));
        clearAuthenticationAttributes(request);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.trace("[LoginProcessingFilter] [unsuccessfulAuthentication].");
        unsuccessfulHandler.onAuthenticationFailure(request, response, exception);
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        log.trace("[LoginProcessingFilter] [clearAuthenticationAttributes].");
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private static UserResponse buildUserResponse(UserEntity user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEnabled(user.isEnabled());
        response.setEmail(user.getUsername());
        response.setName(user.getName());

        return response;
    }

}
