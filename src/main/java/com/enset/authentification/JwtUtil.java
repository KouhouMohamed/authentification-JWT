package com.enset.authentification;

import com.auth0.jwt.algorithms.Algorithm;

public class JwtUtil {
    public static final String SECRET = "MySecret";
    public static final String AUTH_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_PATH = "/refreshToken";
    public static final String TOKEN_START = "Bearer ";
    public static final Long EXPIRATION_ACCESS_TOKEN = 300000L;
    public static final Long EXPIRATION_REFRESH_TOKEN = 1800000L;
}
