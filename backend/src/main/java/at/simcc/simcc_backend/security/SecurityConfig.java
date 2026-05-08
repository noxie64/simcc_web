package at.simcc.simcc_backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.List;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 27.03.2026
 * Time: 11:15
 */
@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Value("${simcc.domain}")
    private String SIMCC_DOMAIN;

    /**
     * Security rules applied:
     *  CSRF protection is disabled (stateless REST API)
     *  CORS is configured via {@link #corsConfigurationSource()}
     *  Public endpoints:
     *      POST /api/users/reg,
     *      GET /api/users/check-if-exist,
     *      GET /api/users/obtain-qr-url,
     *      GET /api/users/verify-2fa,
     *      GET /api/users/login-user"
     *  All other endpoints require authentication
     *  HTTP Basic and form-based login are disabled
     * @param http -> is used for setting all http configurations
     * @return
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/users/reg", "/api/users/check-if-exist").permitAll()
                        .requestMatchers("/api/infected/reg").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(
                            "/api/users/obtain-qr-url",
                            "/api/users/verify-2fa",
                            "/api/users/login-user"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }



    /**
     * Configures CORS (Cross-Origin Resource Sharing) for the application.
     * CORS rules applied:
     *  Allowed origin: http://localhost:5173
     *  Allowed methods: GET, POST, PUT, DELETE
     *  Allowed headers: all headers are permitted
     *  Applied to all endpoints:
     * @return
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Connection",
                "Upgrade"
        ));
        return new UrlBasedCorsConfigurationSource(){{
           registerCorsConfiguration("/**", config);
        }};
    }
}
