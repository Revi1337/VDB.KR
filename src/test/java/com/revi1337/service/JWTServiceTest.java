package com.revi1337.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;


//@TestPropertySource(locations = "classpath:application.yaml")
@DisplayName(value = "[JJWT TEST]")
class JWTServiceTest {

    final String accessTokenSecretKey = "28LT5npRvpBLPrB8WtNbADw3TbeRL6pHEur3K4WZI5/B3GZLIi3nvld3NSsckJREKBp3aIfcJCHE";

    final String refreshTokenSecretKey = "7OQVSJnV7CLILrOZ1rjI8gdyAR9IqPHJYigaNJmnYeGWWfblBtXttMosQoIyVD1eUjv1PFIkhFmk";

    final long accessTokenExpiration = 2000;

    final long refreshTokenExpiration = 5000;

//    ///////////////////////////////////// EXTRACT & VALIDATE /////////////////////////////////////

    public String getDecodedTokenExcludeSignature(String jwt) {
        return Arrays.stream(jwt.split("\\."))
                .limit(2)
                .map(Base64.getDecoder()::decode)
                .map(String::new)
                .collect(Collectors.joining(","));
    }

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
        return isRefreshTokenExpired(refreshToken);
    }

    /**
     * Check AccessToken is expired
     * @param accessToken
     * @return
     */
    public boolean isAccessTokenExpired(String accessToken) {
        return isTokenExpired(accessToken, JWTService.TokenType.ACCESS);
    }

    /**
     * Check RefreshToken is expired
     * @param refreshToken
     * @return
     */
    public boolean isRefreshTokenExpired(String refreshToken) {
        return isTokenExpired(refreshToken, JWTService.TokenType.REFRESH);
    }

    private boolean isTokenValid(String token, JWTService.TokenType tokenType) {
        return !isTokenExpired(token, tokenType);
    }

    private boolean isTokenExpired(String token, JWTService.TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    public Date extractExpiration(String token, JWTService.TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, JWTService.TokenType tokenType, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, JWTService.TokenType tokenType) {
        String secretKey = accessTokenSecretKey;
        if (tokenType.equals(JWTService.TokenType.REFRESH)) secretKey = refreshTokenSecretKey;
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

    @DisplayName(value = "[JWT 테스트 : Subject Claim]")
    @Test
    public void generateAccessTokenTest() {
        String accessToken = generateAccessToken("revi1337");
        System.out.println("jwt = " + accessToken);

        String decodedToken = getDecodedTokenExcludeSignature(accessToken);
        System.out.println("decodedToken = " + decodedToken);
    }

    @DisplayName(value = "[JWT 테스트 : Subject with Extra Claim]")
    @Test
    public void generateAccessTokenTest2() {
        Map<String, String> dummyMap = Map.of(
                "dummy1", "value1", "dummy2", "value2", "dummy3", "value3", "dummy4", "value4");
        String accessToken = generateAccessToken("revi1337", dummyMap);
        System.out.println("jwt = " + accessToken);

        boolean accessTokenExpired = isAccessTokenExpired(accessToken);
        System.out.println("accessTokenExpired = " + accessTokenExpired);
    }


}