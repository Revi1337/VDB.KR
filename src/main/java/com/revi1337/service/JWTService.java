package com.revi1337.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Value("${application.security.jwt.secret-key}")
    private String accessTokenSecretKey;

    @Value("${application.security.jwt.refresh-token.secret-key}")
    private String refreshTokenSecretKey;

    @Value("${application.security.jwt.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String getDecodedTokenExcludeSignature(String jwt) {
        return Arrays.stream(jwt.split("\\."))
                .limit(2)
                .map(Base64.getDecoder()::decode)
                .map(String::new)
                .collect(Collectors.joining(","));
    }

//    ///////////////////////////////////// EXTRACT & VALIDATE /////////////////////////////////////

    /**
     * Check AccessToken has not expired && Signature has not been modified
     * @param accessToken
     * @return
     */
    public boolean isAccessTokenValid(String accessToken) {
        return !isAccessTokenExpired(accessToken);
    }

    /**
     * Check refreshToken is expired && Signature has not been modified
     * @param refreshToken
     * @return
     */
    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            isRefreshTokenExpired(refreshToken);
        } catch (ExpiredJwtException exception) {
            return true;
        }
        return false;
    }

    /**
     * Check AccessToken is expired
     * @param accessToken
     * @return
     */
    public boolean isAccessTokenExpired(String accessToken) {
        return isTokenExpired(accessToken, TokenType.ACCESS);
    }

    /**
     * Check RefreshToken is expired
     * @param refreshToken
     * @return
     */
    public boolean isRefreshTokenExpired(String refreshToken) {
        return isTokenExpired(refreshToken, TokenType.REFRESH);
    }

    private boolean isTokenValid(String token, TokenType tokenType) {
        return !isTokenExpired(token, tokenType);
    }

    private boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    public Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType tokenType) {
        String secretKey = accessTokenSecretKey;
        if (tokenType.equals(TokenType.REFRESH)) secretKey = refreshTokenSecretKey;
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    ////////////////////////////// GENERATE & BUILD //////////////////////////////

    public String generateAccessToken(String subject) {
        return generateAccessToken(subject, new HashMap<>());
    }

    public String generateAccessToken(String subject, Map<String, ?> extraClaims) {
        return buildToken(subject,
                extraClaims,
                accessTokenExpiration);
    }

    public String generateRefreshToken(String subject) {
        return buildToken(subject,
                new HashMap<>(),
                refreshTokenExpiration,
                getSigningKey(refreshTokenSecretKey));
    }

    private String buildToken(String subject, Map<String, ?> extraClaims, long expiration) {
        return buildToken(subject,
                extraClaims,
                expiration,
                getSigningKey(accessTokenSecretKey));
    }

    private String buildToken(String subject, Map<String, ?> extraClaims, long expiration, Key key) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuer("VDB.KR")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSigningKey(String keyType) {
        return Keys.hmacShaKeyFor(keyType.getBytes());
    }

//    ///////////////////////////////////// INNER ENUM /////////////////////////////////////

    public enum TokenType {
        ACCESS,
        REFRESH
    }

    //    private String buildToken(String subject, Map<String, ?> extraClaims, long expiration) {
//        return Jwts.builder()
//                .setSubject(subject)
//                .setClaims(extraClaims)
//                .setIssuer("VDB.KR")
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSigningKey(accessTokenSecretKey), SignatureAlgorithm.HS512)
//                .compact();
//    }

//    public String generateRefreshToken(String subject) {
//        return Jwts.builder()
//                .setSubject(subject)
//                .setIssuer("VDB.KR")
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
//                .signWith(getSigningKey(refreshTokenSecretKey), SignatureAlgorithm.HS512)
//                .compact();
//    }

//    ///////////////////////////////////// EXTRACT & VALIDATE /////////////////////////////////////
//
//    public String extractSubject(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    ///////////////////////////////////// GENERATE & SIGN /////////////////////////////////////
//
//    public String generateToken(String subject) {
//        return generateToken(new HashMap<>(), subject);
//    }
//
//    public String generateToken(
//            Map<String, ?> extraClaims,
//            String subject
//    ) {
//        return buildToken(extraClaims, subject, jwtExpiration);
//    }
//
//    public String generateRefreshToken(String subject) {
//        return buildToken(new HashMap<>(), subject, refreshExpiration);
//    }
//
//    private String buildToken(
//            Map<String, ?> extraClaims,
//            String subject,
//            long expiration
//    ) {
//        return Jwts
//                .builder()
//                .setClaims(extraClaims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

}