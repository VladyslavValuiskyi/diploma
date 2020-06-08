package com.proarea.api.security.web;

import com.proarea.api.security.jwt.AuthenticationToken;
import com.proarea.api.security.jwt.JwtHeaderTokenExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private WebAuthenticationUnsuccessfulHandler unsuccessfulHandler;

    public TokenAuthenticationFilter(RequestMatcher pathMatcher, WebAuthenticationUnsuccessfulHandler unsuccessfulHandler) {
        super(pathMatcher);
        this.unsuccessfulHandler = unsuccessfulHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.trace("[TokenAuthenticationFilter] [attemptAuthentication].");
        String tokenPayload = request.getHeader(JwtHeaderTokenExtractor.JWT_TOKEN_HEADER_PARAM);
        return getAuthenticationManager().authenticate(new AuthenticationToken(JwtHeaderTokenExtractor.extract(tokenPayload)));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        log.trace("[TokenAuthenticationFilter] [successfulAuthentication].");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.trace("[TokenAuthenticationFilter] [unsuccessfulAuthentication].");
        this.unsuccessfulHandler.onAuthenticationFailure(request, response, exception);
    }

}
