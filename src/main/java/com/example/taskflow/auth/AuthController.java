package com.example.taskflow.auth;

import com.example.taskflow.security.JwtService;
import com.example.taskflow.user.User;
import com.example.taskflow.user.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public record AuthRequest(@NotBlank String username, @NotBlank String password) {}
  public record AuthResponse(String token) {}

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody AuthRequest req) {
    if (userRepository.existsByUsername(req.username())) {
      return ResponseEntity.badRequest().body("username already exists");
    }

    User user = User.builder()
        .username(req.username())
        .password(passwordEncoder.encode(req.password()))
        .build();

    userRepository.save(user);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest req) {
    var userOpt = userRepository.findByUsername(req.username());
    if (userOpt.isEmpty()) return ResponseEntity.status(401).body("invalid credentials");

    var user = userOpt.get();
    if (!passwordEncoder.matches(req.password(), user.getPassword())) {
      return ResponseEntity.status(401).body("invalid credentials");
    }

    String token = jwtService.generateToken(user.getUsername());
    return ResponseEntity.ok(new AuthResponse(token));
  }
}
