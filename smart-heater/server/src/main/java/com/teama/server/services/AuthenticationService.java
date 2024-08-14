package com.teama.server.services;

import com.teama.server.dto.authentication.AuthenticationRequest;
import com.teama.server.dto.authentication.AuthenticationResponse;
import com.teama.server.dto.register.RegisterRequest;
import com.teama.server.enums.Role;
import com.teama.server.models.User;
import com.teama.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    User user = new User(null, request.getEmail(), request.getName(), Role.TENANT, passwordEncoder.encode(request.getPassword()));

    userRepository.save(user);
    String jwtToken = jwtService.generateToken(user);

    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) throws BadCredentialsException {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
    String jwtToken = jwtService.generateToken(user);

    return new AuthenticationResponse(jwtToken);
  }
}
