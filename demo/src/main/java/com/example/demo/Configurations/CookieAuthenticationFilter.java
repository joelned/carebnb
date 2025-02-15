package com.example.demo.Configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

public class CookieAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public CookieAuthenticationFilter(RequestMatcher matcher, JwtDecoder jwtDecoder,
                                      JwtAuthenticationConverter jwtAuthenticationConverter) {
        super(matcher);
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }


    @Override
    public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String URI = request.getRequestURI();
        if(URI.contains("/api/v1/auth/login") ||
                URI.matches("/api/v1/auth/.*/register") || URI.equals("/login") ||
                URI.equals("/favicon.ico" ) || URI.equals("/login?error=true") || URI.equals("/")
        ){
            return false;
        }
        return super.requiresAuthentication(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = getJwtFromCookie(request);

        if (token == null) {
            throw new AuthenticationException("No Jwt Found in Cookie") {
            };
        }
        Jwt jwt = jwtDecoder.decode(token);
        return jwtAuthenticationConverter.convert(jwt);

    }

    private String getJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        System.out.println(SecurityContextHolder.getContext());
        chain.doFilter(request, response);
    }

}
