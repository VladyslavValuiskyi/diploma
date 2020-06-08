package com.proarea.api.security.jwt;


import org.apache.commons.lang3.StringUtils;

public final class JwtHeaderTokenExtractor {

    public static final String JWT_TOKEN_HEADER_PARAM = "Authorization";
    public static final String JWT_TOKEN_HEADER_PREFIX = "Bearer ";

    private JwtHeaderTokenExtractor() {

    }

    public static String extract(String header) {
        if (StringUtils.isEmpty(header)) {
            return null;
        }
        if (header.length() < JWT_TOKEN_HEADER_PREFIX.length()) {
            return null;
        }
        if (!header.contains(JWT_TOKEN_HEADER_PREFIX)) {
            return null;
        }
        return header.substring(JWT_TOKEN_HEADER_PREFIX.length(), header.length());
    }

}
