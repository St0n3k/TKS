package pl.lodz.p.it.tks.user.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.tks.user.exception.security.JwtException;
import pl.lodz.p.it.tks.user.infrastructure.command.JwtCommandPort;

import java.util.Date;

@ApplicationScoped
public class JwtProvider implements JwtCommandPort {

    @Inject
    @ConfigProperty(name = "pl.lodz.pas.security.jwt.secret")
    private String secret;

    //JWT expiration time = 1h
    @Inject
    @ConfigProperty(name = "pl.lodz.pas.security.jwt.expirationTime",
        defaultValue = "3600000")
    private int jwtExpirationTime;

    public String generateJWT(String subject, String role) {
        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(new Date())
            .claim("role", role)
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();

    }

    private Jws<Claims> parseJWT(String jwt) throws JwtException {
        try {
            return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt);
        } catch (SignatureException e) {
            throw new JwtException();
        }
    }

    public boolean validateToken(String jwt) {
        try {
            parseJWT(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSubject(String jwt) throws JwtException {
        return parseJWT(jwt).getBody().getSubject();
    }

    @Override
    public String getRole(String jwt) throws JwtException {
        return parseJWT(jwt).getBody().get("role", String.class);
    }


}
