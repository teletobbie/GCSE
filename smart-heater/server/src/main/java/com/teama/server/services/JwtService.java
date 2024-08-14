package com.teama.server.services;

import com.teama.server.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {
  // https://www.grc.com/passwords.htm 64 hex character for a 256 binary bit Encryption Key
  private static final String SECRET_KEY = "7B5B00B103F9660A988D7B1A298198A47CA14F764BA32872AE47377CEFC83D9B";
  private static final long EXPIRATION = 1000 * 60 * 24; // 1000ms, 60 to hours, 24 to one day

  public Optional<String> extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Generic Method to extract anything from the Claims
  public <T> Optional<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
    Optional<Claims> claims = extractAllClaims(token);
    return claims.map(claimsResolver);
  }

  public String generateToken(User user) {
    HashMap<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("role", user.getRole());
    extraClaims.put("name", user.getName());
    extraClaims.put("id", user.getId());
    return generateToken(extraClaims, user);
  }

  // Generates a token that lasts for EXPIRATION ms with our Sign-in-Key
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  // Validate if the token belongs to the userDetails
  public boolean isTokenValid(String token, UserDetails userDetails) {
    Optional<String> username = extractUsername(token);
    return username.isPresent() && username.get().equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    Optional<Date> expiration = extractExpiration(token);
    return expiration.isPresent() && expiration.get().before(new Date());
  }

  private Optional<Date> extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  // I'm really unhappy with this method
  private Optional<Claims> extractAllClaims(String token) {
    try {
      return Optional.of(Jwts
              .parserBuilder()
              .setSigningKey(getSignInKey())
              .build()
              .parseClaimsJws(token)
              .getBody());
    } catch (ExpiredJwtException ex) {
      return Optional.empty();
    }
  }

  // The SignInKey is a secret to digitally sign the JWT,
  // which is used to verify if the sender is who he claims to be,
  // and to ensure the message has not been changed.
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}