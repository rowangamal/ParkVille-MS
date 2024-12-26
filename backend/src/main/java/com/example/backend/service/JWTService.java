package com.example.backend.service;

//import com.example.backend.dtos.AuthUserInfo;
//import com.example.backend.entities.UserDetail;
import com.example.backend.model.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration.days}")
    private Long EXPIRATION_DAYS;

    public String generateToken(int userID, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("id", userID);
        return Jwts.builder()
                .claims()
                .add(claims)
//                .subject(String.valueOf(userID))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 24 * 60 * 60 * EXPIRATION_DAYS))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey(){
        byte[] secretBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetail) {
        int userId = ((CustomUserDetails) userDetail).getUserId();  // Use CustomUserDetails here
        return userId == extractUserId(token) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class); // Extract the 'role' claim
    }

    public int extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("id", Integer.class); // Extract the 'id' claim
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public void setSECRET_KEY(String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }

    public void setEXPIRATION_DAYS(Long EXPIRATION_DAYS) {
        this.EXPIRATION_DAYS = EXPIRATION_DAYS;
    }
}
