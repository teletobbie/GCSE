package com.teama.server.filters;

import com.teama.server.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Automatically gets used as a filter, once per request!
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserDetailsService userDetailsService; // This is why User implements UserDetails

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization"); // Header that contains the JWT
    final String jwt;
    final Optional<String> username;

    // Exit early if the authHeader is incorrect.
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // Substring to remove the "Bearer "
    jwt = authHeader.substring(7);
    // Exception not being handled when extracting username without a token
    username = jwtService.extractUsername(jwt); // Naming convention in Security is for this to be called Username

    if (username.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username.get());
      if (jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // Continue to the next filter
    filterChain.doFilter(request, response);
  }
}
