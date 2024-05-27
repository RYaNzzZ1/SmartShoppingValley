package com.smartshoppingvalley.tools;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${jwt.expiration}")
//    private int jwtExpiration;

    public static String generateToken(String id) {
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(SignatureAlgorithm.HS256, "2xDRaUVK13Y0LkMmTg0zIBkrPPO9z3Ch")
                .compact();
    }

    public static String getIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("2xDRaUVK13Y0LkMmTg0zIBkrPPO9z3Ch")
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // 获取负载中的用户名
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("2xDRaUVK13Y0LkMmTg0zIBkrPPO9z3Ch")
                    .parseClaimsJws(token)
                    .getBody();
            Date expirationDate = claims.getExpiration();
            return !expirationDate.before(new Date());
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            return false;
        }
    }


}
