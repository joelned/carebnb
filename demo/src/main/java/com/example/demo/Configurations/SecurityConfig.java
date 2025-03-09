package com.example.demo.Configurations;

import com.example.demo.Services.UserDetailsServiceImpl;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final RsaKeyProperties rsaKeys;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;
    public SecurityConfig(RsaKeyProperties rsaKeys, UserDetailsServiceImpl userDetailsService) {
        this.rsaKeys = rsaKeys;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception{
         return http
                 .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/*/register","/api/v1/auth/test","/api/v1/auth/login",
                                "/css/**", "/js/**",
                                "/images/**", "/", "/signup",
                                "/signup/**", "/favicon.ico", "/login", "/get-started",
                                "/api/v1/auth/register", "/error").permitAll()
                        .anyRequest().authenticated())

                 .oauth2ResourceServer(oauth -> oauth
                         .jwt(Customizer.withDefaults())
                 )
                 .formLogin(form -> form
                         .loginPage("/login")
                         .successHandler(customSuccessHandler)
                         .permitAll()
                 )
                 .addFilterBefore(new CookieAuthenticationFilter(
                         matcher(),
                         jwtDecoder(),
                         converter()),
                         UsernamePasswordAuthenticationFilter.class
                 )
                 .addFilterBefore(new AnonymousAuthenticationFilter("annonymousKey"), CookieAuthenticationFilter.class)
                 .anonymous(AbstractHttpConfigurer::disable)
                 .build();

    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
    throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RequestMatcher matcher(){
        return new AntPathRequestMatcher("/**");
    }

    @Bean
    public JwtAuthenticationConverter converter(){
        return new JwtAuthenticationConverter();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }


    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
