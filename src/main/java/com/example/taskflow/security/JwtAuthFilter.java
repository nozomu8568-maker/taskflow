package com.example.taskflow.security;

import com.example.taskflow.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    String auth = request.getHeader("Authorization");
    if (auth == null || !auth.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = auth.substring(7);

    String username;
    try {
      username = jwtService.extractUsername(token);
    } catch (Exception e) {
      // トークン不正・期限切れなどは何もせずスルー（=未認証扱い）
      filterChain.doFilter(request, response);
      return;
    }

    // username がDBに存在する場合のみ認証OK
    if (userRepository.findByUsername(username).isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    var authentication = new UsernamePasswordAuthenticationToken(
        username,
        null,
        List.of(new SimpleGrantedAuthority("ROLE_USER"))
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }
}