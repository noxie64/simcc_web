package at.simcc.simcc_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    /**
     * Security rules applied:
     *  CSRF protection is disabled (stateless REST API)
     *  CORS is configured via {@link #corsConfigurationSource()}
     *  Public endpoints:
     *      POST /users/reg,
     *      GET /users/check-if-exist,
     *      GET /users/obtain-qr-url,
     *      GET /users/verify-2fa,
     *      GET /users/login-user,
     *      GET /users/verify-2fa-after-login
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
                        .requestMatchers("/infected/reg", "/error").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/trojan/**").permitAll()
                        .requestMatchers("/infected/allInfected").authenticated()
                        .anyRequest().authenticated()
                )
                .securityContext(context -> context.requireExplicitSave(false))
                .logout(logout ->
                        logout.logoutUrl("/users/logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID"));

        return http.build();
    }



    /**
     * Configures CORS (Cross-Origin Resource Sharing) for the application.
     * CORS rules applied:
     *  Allowed origin: http://localhost:5173
     *  Allowed methods: GET, POST, PUT, DELETE
     *  Allowed headers: Authorization,
     *                 Content-Type,
     *                 Connection,
     *                 Upgrade,
     *                 Cookie
     *  Applied to all endpoints:
     * @return
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowCredentials(true);

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Connection",
                "Upgrade",
                "Cookie"
        ));
        return new UrlBasedCorsConfigurationSource(){{
           registerCorsConfiguration("/**", config);
        }};
    }
}
