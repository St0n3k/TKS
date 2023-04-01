package pl.lodz.p.it.tks.infrastructure.command;

import pl.lodz.p.it.tks.exception.security.JwtException;

public interface JwtCommandPort {

    boolean validateToken(String jwt);

    String getSubject(String jwt) throws JwtException;

    String getRole(String jwt) throws JwtException;
}
