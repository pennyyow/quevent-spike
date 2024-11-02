package com.quevent.backend.config;

import com.quevent.backend.util.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private final String publicKeyString = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA98sXknKV4pa2huVXbplr5AFaYoQe/uuEIqKwqUpSuSrmz4rBOuXyw+gHbVP0WCr1k6H8qA2d6IUeO6ZD2j1pOWwpHRzocOpYIq9K8uV4o0eP8WPDY2GrOLLOANrxJcAa4lkJS1l4vfUt8WuONNqo/sqtz3LwfRaGjQM4iPPhJ/oT29yI99HYFrW1YEcSozAugLf2DPbX3e7EKB58HtYUJs8Y2thAgXayELAYq0pChNbvsjwUf7RxqtOSJp7s6WR017/qm7QreCGTDWZIjc82ZJLEbk+mEEmRZejUo6UDmsRRF/jTwpg7C3sMyw5uJv2ob369XPX+9uP81Pko3/pIZQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/graphql").authenticated()
                .anyRequest().authenticated()
        )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    try {
                        jwt.decoder(jwtDecoder()); // Use the JWT Decoder
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()); // Use the custom JWT Authentication Converter
                }))
                .csrf(csrf -> csrf.disable());
//                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/auth/**", "/graphql")); // Disable CSRF for registration/login

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        RSAPublicKey publicKey = KeyUtil.getPublicKeyFromString(publicKeyString);
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName("sub");
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
