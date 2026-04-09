package com.example.demo.Infrastructure.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.Application.auth.dto.JwtPayload;
import com.example.demo.Application.auth.dto.RefreshTokenPayload;

import com.example.demo.Domain.Interfaces.TokenProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
// import lombok.Value;

@Component
public class JwtTokenProvider implements TokenProvider{

   @Value("${JWT_SECRET}")
    private String SECRET;

    @Override
    public  String generateToken(JwtPayload jwtTokenData){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,jwtTokenData);
    }

    @Override
    public String genereateRefreshToken(RefreshTokenPayload refreshTokenPayload) {
        Map<String ,Object> claims=new HashMap<>();
        return Jwts.builder()
        .setId(String.valueOf(refreshTokenPayload.id()))
        .setSubject(String.valueOf(refreshTokenPayload.userId()))
        .claim("userId", refreshTokenPayload.userId())
        .setExpiration(refreshTokenPayload.expireAt())
        .setIssuedAt(refreshTokenPayload.createdAt())
         .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
        

    }

    @Override
    public RefreshTokenPayload RefreshTokenDecodedPayload(String refreshToken) {
       Claims refreshTokenData=extractAllClaims(refreshToken);
        String jtiString = refreshTokenData.getId();
        String subString = refreshTokenData.getSubject();
        
        // 2. Convert Strings back to UUIDs
        UUID refreshTokenId = UUID.fromString(jtiString);
        UUID userId = UUID.fromString(subString);
       return RefreshTokenPayload.map(userId, 
        refreshTokenId,
         refreshTokenData.getIssuedAt(),
          refreshTokenData.getExpiration());
    }
      private String createToken(Map<String, Object> claims, JwtPayload jwtTokenData) {
       return Jwts.builder()
       .setClaims(claims)
       .setSubject(String.valueOf(jwtTokenData.id()))
       .claim("username", jwtTokenData.username())
       .claim("email", jwtTokenData.email())
       .setIssuedAt(new Date())
       .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
       .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserID(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRefreshTokenId(String token){
        // the refrehs token id is in the Subject
        return extractClaim(token, Claims::getId);
    }

   
    
   
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

   public Boolean validateToken(String token,CustomUserDetails userDetails){
    // Load the user Id as string 
        final String userId = extractUserID(token);
        return (userId.equals(String.valueOf(userDetails.getUserId())) && !isTokenExpired(token));
   }
}
