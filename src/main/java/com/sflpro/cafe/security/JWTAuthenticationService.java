package com.sflpro.cafe.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sflpro.cafe.enumeration.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationService {

    public static final String TOKEN_NAME = "jwt-auth-token";

    private static final String SECRET = "gTTjxjlqsSO1WxF5PZMcaZQbAfOvEl3g";
    private static final String ID = "id";
    private static final String SUBJECT = "sub";
    private static final String ENABLED = "enabled";
    private static final String AUTHORITIES = "authorities";
    private final com.sflpro.cafe.security.JwtProperties jwtProperties;

    public JWTAuthenticationService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAuthHeader(Authentication auth) {

        final String[] authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        return JWT.create()
                .withClaim(ID, principal.getId())
                .withClaim(SUBJECT, principal.getUsername())
                .withClaim(ENABLED, principal.isEnabled())
                .withArrayClaim(AUTHORITIES, authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public Authentication parseAuthHeader(String authToken) {

        if (authToken == null) {
            return null;
        }

        UserPrincipal userPrincipal = extractUserPrincipal(authToken);

        List<SimpleGrantedAuthority> authorities = extractAuthorities(authToken);

        return new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
    }


    public static UserPrincipal extractUserPrincipal(String authToken) {

        DecodedJWT decodedJWT = JWTAuthenticationService.decodeJwt(authToken);
        Map<String, Claim> claims = decodedJWT.getClaims();

        String username = claims.get(JWTAuthenticationService.SUBJECT).asString();
        Boolean enabled = claims.get(JWTAuthenticationService.ENABLED).asBoolean();
        Long id = claims.get(ID).asLong();

        List<SimpleGrantedAuthority> authorities = extractAuthorities(authToken);

        Role role = authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .map(Role::valueOf)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);


        return UserPrincipal.builder()
                .id(id)
                .username(username)
                .role(role)
                .enabled(enabled)
                .build();
    }

    public static List<SimpleGrantedAuthority> extractAuthorities(String authToken) {

        DecodedJWT decodedJWT = JWTAuthenticationService.decodeJwt(authToken);
        Map<String, Claim> claims = decodedJWT.getClaims();

        @SuppressWarnings("unchecked") final List<String> authoritiesClaim = claims.get(AUTHORITIES).asList(String.class);

        return authoritiesClaim
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static DecodedJWT decodeJwt(String authToken) {

        JWTVerifier verifier = buildVerifier();

        return verifier.verify(authToken);
    }

    public static JWTVerifier buildVerifier() {

        return JWT.require(Algorithm.HMAC256(SECRET))
                .build();
    }
}
