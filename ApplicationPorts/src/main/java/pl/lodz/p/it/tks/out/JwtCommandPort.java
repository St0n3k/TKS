package pl.lodz.p.it.tks.out;

import pl.lodz.p.it.tks.exception.security.JwtException;

public interface JwtCommandPort {
    String generateJWT(String subject, String role);

    boolean validateToken(String jwt);

    String getSubject(String jwt) throws JwtException;
    String getRole(String jwt) throws JwtException;

}
