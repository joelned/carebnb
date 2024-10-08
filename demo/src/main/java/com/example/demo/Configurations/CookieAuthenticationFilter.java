package com.example.demo.Configurations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class CookieAuthenticationFilter extends AbstractAuthenticationProcessingFilter{
    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
   private JwtAuthenticationConverter jwtAuthenticationConverter;

     CookieAuthenticationFilter(RequestMatcher matcher){
       super(matcher);
    }

    @Override
    public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response){
        String URI = request.getRequestURI();
        return !URI.contains("/api/v1/auth/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
         String token = getJwtFromToken(request);
         if(token == null){
             throw new AuthenticationException("No Jwt Found"){};
         }
         Jwt jwt = jwtDecoder.decode(token);
         return jwtAuthenticationConverter.convert(jwt);
    }

    public String getJwtFromToken(HttpServletRequest request){
        if(request.getCookies()!= null){
            for(Cookie cookie: request.getCookies()){
                if("jwt".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
