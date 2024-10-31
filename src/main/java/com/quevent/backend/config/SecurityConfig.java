package com.quevent.backend.config;

import com.quevent.backend.util.KeyUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String publicKeyString = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiYw4A0FoiKNFzj2SlZ48PZegjYEWxHHKvj+qSHCsf9tgoy8CAeMio8R11W7MiAgOF1r/KUWi0PDjMhwfa7zzFtdJ598Gahhq1HWyl4sb6X2YCA8IuR7Lt1vHRAbX7jbPO9+1+fCpdXEp/7Sp06D2fswuatGVSt+fjdPRQVWCB0izoEKSuUJ+1lBD6a+/uwYyxlueWfY+E4mFpdhlBrdsYgL7HBuERaNd/+niQJZkCKXksImCqeGwIeEQh7hF1+0IIc0mOZd1js7cx4/UGJhvHkcp701J8d6VjpsL8Yhlas68x0KWUYkiutb3s+g6GkQEuXFNXrG8WHkdSYoI8UDPZwIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register").permitAll()
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
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/auth/register")); // Disable CSRF for registration

        return http.build();
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

//    @Bean
//    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
}