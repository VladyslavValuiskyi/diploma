package com.proarea.api.security.jwt;

import com.proarea.api.model.entity.AuthoritiesEntity;
import com.proarea.api.repository.AuthoritiesRepository;
import com.proarea.api.security.model.UserDetails;
import com.proarea.api.security.model.UserResponse;
import com.proarea.api.security.web.Messages;
import com.proarea.api.util.DateUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenBuilder {

    public static final String CLAIM_AUTHORITIES = "scopes";
    public static final String USER = "user";

    private final JwtSettings settings;
    private AuthoritiesRepository authoritiesRepository;

    public JwtTokenBuilder(JwtSettings settings, AuthoritiesRepository authoritiesRepository) {
        this.settings = settings;
        this.authoritiesRepository = authoritiesRepository;
    }

    public String encodeToken(UserResponse userResponse) {
        log.trace("[JwtTokenBuilder] [encodeToken] [{}].", userResponse);
        if (StringUtils.isEmpty(userResponse.getEmail()))
            throw new AuthenticationServiceException(Messages.TOKEN_ENCODE_ERROR_NO_USERNAME);

        Claims claims = Jwts.claims().setSubject(userResponse.getEmail());
        claims.put(USER, userResponse);
        claims.put(CLAIM_AUTHORITIES, UserDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
        LocalDateTime currentTime = DateUtils.now();
        return Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer()).setIssuedAt(DateUtils.toDate(currentTime))
                .setExpiration(DateUtils.toDate(currentTime.plusMinutes(settings.getTokenExpirationMinutes())))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();
    }

    public String encodeToken(UserResponse userResponse, User user) {
        log.trace("[JwtTokenBuilder] [encodeToken] [{}].", user);
        if (StringUtils.isEmpty(user.getUsername()))
            throw new AuthenticationServiceException(Messages.TOKEN_ENCODE_ERROR_NO_USERNAME);
        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            throw new AuthenticationServiceException(Messages.TOKEN_ENCODE_ERROR_NO_PRIVILEGES);
        }
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put(USER, userResponse);
        claims.put(CLAIM_AUTHORITIES, authoritiesRepository.findAllByUserId(userResponse.getId()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        claims.put(CLAIM_AUTHORITIES, user.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
        LocalDateTime currentTime = DateUtils.now();
        return Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer()).setIssuedAt(DateUtils.toDate(currentTime))
                .setExpiration(DateUtils.toDate(currentTime.plusMinutes(settings.getTokenExpirationMinutes())))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();
    }

    @SuppressWarnings("unchecked")
    public User decodeToken(String token) {
        log.trace("[JwtTokenBuilder] [decodeToken].");

        try {
            Jws<Claims> claims = Jwts.parser().requireIssuer(settings.getTokenIssuer()).setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token);
            String username = claims.getBody().getSubject();
            List<String> scopes = claims.getBody().get(CLAIM_AUTHORITIES, List.class);

            List<GrantedAuthority> authorities = scopes
                    .stream()
                    .map((Function<Object, GrantedAuthority>) linkedHashMap -> () -> ((LinkedHashMap) linkedHashMap).get("authority").toString())
                    .collect(Collectors.toList());
//            List<GrantedAuthority> authorities = new ArrayList<>(Collections.singleton((GrantedAuthority) () -> "ROLE_USER"));
            User user = new User(username, "", authorities);
            log.debug("[JwtTokenBuilder] [decodeToken] [{}].", user);
            return user;
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            log.trace("[JwtTokenBuilder] {}.", Messages.JWT_TOKEN_EXPIRED);
            throw new AuthenticationServiceException(Messages.JWT_TOKEN_EXPIRED, e);
        } catch (JwtException e) {
            log.trace("[JwtTokenBuilder] {}.", Messages.JWT_TOKEN_INVALID);
            throw new BadCredentialsException(Messages.JWT_TOKEN_INVALID, e);
        }
    }

}
