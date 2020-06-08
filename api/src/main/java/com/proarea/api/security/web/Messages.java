package com.proarea.api.security.web;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Messages {

    public static final String USERNAME_SHOULD_BE_PROVIDED = "Username should be provided.";

    public static final String JWT_TOKEN_EXPIRED = "JWT token expired";
    public static final String JWT_TOKEN_INVALID = "Invalid JWT token";

    public static final String AUTHENTICATION_METHOD_NOT_SUPPORTED = "Authentication method not supported.";
    public static final String NO_CREDENTIALS = "Credentials not provided. Username or Password not provided.";
    public static final String TOKEN_SHOULD_BE_PROVIDED = "Token should be provided.";
    public static final String USER_WAS_DELETED = "User was deleted.";

    public static final String TOKEN_ENCODE_ERROR_NO_USERNAME = "Cannot create JWT Token without username.";
    public static final String TOKEN_ENCODE_ERROR_NO_PRIVILEGES = "User doesn't have any privileges.";

}
