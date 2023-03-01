package pl.lodz.p.it.tks.ui;

import pl.lodz.p.it.tks.exception.security.JwtException;

public interface JwtUseCase {
    boolean validateToken(String jwt);

    String getSubject(String jwt) throws JwtException;

    String getRole(String jwt) throws JwtException;
}
