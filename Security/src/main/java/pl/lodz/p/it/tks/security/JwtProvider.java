package pl.lodz.p.it.tks.security;


import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.tks.exception.security.JwtException;
import pl.lodz.p.it.tks.infrastructure.command.JwtCommandPort;

@ApplicationScoped
public class JwtProvider implements JwtCommandPort {

    @Inject
    @ConfigProperty(name = "pl.lodz.pas.security.jwt.secret")
    private String SECRET;

    //JWT expiration time = 1h
    @Inject
    @ConfigProperty(name = "pl.lodz.pas.security.jwt.expirationTime",
                    defaultValue = "3600000")
    private int JWT_EXPIRATION_TIME;

    public String generateJWT(String subject, String role) {
        return Jwts.builder()
                   .setSubject(subject)
                   .setIssuedAt(new Date())
                   .claim("role", role)
                   .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                   .signWith(SignatureAlgorithm.HS512, SECRET)
                   .compact();

    }

    private Jws<Claims> parseJWT(String jwt) throws JwtException {
        try {
            return Jwts.parser()
                       .setSigningKey(SECRET)
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
