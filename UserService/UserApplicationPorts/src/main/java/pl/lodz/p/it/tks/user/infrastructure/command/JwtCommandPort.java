package pl.lodz.p.it.tks.user.infrastructure.command;

import pl.lodz.p.it.tks.user.exception.security.JwtException;

public interface JwtCommandPort {
    String generateJWT(String subject, String role);

    boolean validateToken(String jwt);

    String getSubject(String jwt) throws JwtException;

    String getRole(String jwt) throws JwtException;
}
