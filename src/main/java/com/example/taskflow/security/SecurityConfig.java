package com.example.taskflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Configuration
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/auth/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/h2-console/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) ->
                writeJsonError(request, response, 401, "UNAUTHORIZED", "Authentication required")
            )
            .accessDeniedHandler((request, response, accessDeniedException) ->
                writeJsonError(request, response, 403, "FORBIDDEN", "Access denied")
            )
        )
        .headers(h -> h.frameOptions(f -> f.disable())) // H2 console
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  private void writeJsonError(
      HttpServletRequest request,
      jakarta.servlet.http.HttpServletResponse response,
      int status,
      String error,
      String message
  ) throws java.io.IOException {

    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Map<String, Object> body = Map.of(
        "error", error,
        "message", message,
        "path", request.getRequestURI()
    );

    response.getWriter().write(objectMapper.writeValueAsString(body));
  }
}