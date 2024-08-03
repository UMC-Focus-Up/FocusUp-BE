package com.focusup.global.security.jwt;

import com.focusup.entity.enums.Role;
import com.focusup.global.apiPayload.exception.CustomException;
import com.focusup.global.apiPayload.exception.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.focusup.global.apiPayload.code.ErrorCode.INVALID_TOKEN;

@Slf4j
@Component
public class JwtTokenUtils {
    private static final long ACCESS_TOKEN_DURATION =  1000 * 60 * 60L * 24; // 1일
    private static final long REFRESH_TOKEN_DURATION = 1000 * 60 * 60L * 24 * 7; // 7일

    @Value("${jwt.secret}")
    private String key;

    private SecretKey secretKey;

    public JwtTokenUtils(@Value("${jwt.secret}") String key) {
        byte[] keyBytes = key.getBytes();
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        String accessToken = generateAccessToken(authentication, authorities);
        String refreshToken = generateRefreshToken();

        return new TokenInfo("Bearer", accessToken, refreshToken);
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date());
    }


    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new CustomException(INVALID_TOKEN, "권한 정보가 없는 토큰입니다.");
        }
        List<SimpleGrantedAuthority> authority = getAuthorities(claims);
        validateAuthorityValue(authority);

        UserDetails principal = new User(claims.getSubject(), "", authority);
        return new UsernamePasswordAuthenticationToken(principal, "", authority);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new TokenException(INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new TokenException(INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new TokenException(INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
            throw new TokenException(INVALID_TOKEN);
        } catch (JwtException e) {
            log.info("JWT Token error: " + e.getMessage());
            throw new TokenException(INVALID_TOKEN);
        }
    }

    private void validateAuthorityValue(List<SimpleGrantedAuthority> authority) {
        if (authority.size() != 1
                || !Role.isValidAuthority(authority.get(0))
        ) {
            throw new CustomException(INVALID_TOKEN, "유효하지 않은 권한 값을 가진 토큰입니다.");
        }
    }

    private String generateAccessToken(Authentication authentication, String authorities) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + ACCESS_TOKEN_DURATION);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken() {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + REFRESH_TOKEN_DURATION);

        return Jwts.builder()
                .setExpiration(expiredDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get("auth").toString()));
    }

}
