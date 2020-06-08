package com.proarea.api.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proarea.api.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class WebAuthenticationUnsuccessfulHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public WebAuthenticationUnsuccessfulHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.trace("[WebAuthenticationUnsuccessfulHandler] [onAuthenticationFailure].");
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        if (exception.getCause() instanceof LockedException) {
            status = HttpStatus.FORBIDDEN;
        }
        log.info("[WebAuthenticationUnsuccessfulHandler] [{}] {}", status.value(), exception.getMessage());
        SecurityContextHolder.clearContext();
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), ErrorResponse.of(exception.getMessage(), 401, request.getRequestURI()));

    }

}
