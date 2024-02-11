package com.blogapp.backend.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.JwtSigner;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

public class JwtUtils {
    private JwtUtils(){}
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    public static boolean validateToken(String jwtToken) {
        return parseToken(jwtToken).isPresent();
    }

    private static Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Optional<Claims> parseToken(String jwtToken) {
        return Optional.ofNullable(Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody());
    }

    public static Optional<String> getUsernameFromToken(String jwtToken) {
        var claimsOptional = parseToken(jwtToken);
        if(claimsOptional.isPresent()){
            return Optional.of(claimsOptional.get().getSubject());
        }
        return Optional.empty();
    }

    public static String generateToken(String username){
        Date now = new Date();
        long cookieExpiry = 150;
        Date expiryDate = new Date(now.getTime() + cookieExpiry*1000L);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiryDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
}
