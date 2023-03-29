package pl.lodz.p.it.tks.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.tks.exception.security.JwtException;
import pl.lodz.p.it.tks.infrastructure.command.JwtCommandPort;
import pl.lodz.p.it.tks.ui.JwtUseCase;

@RequestScoped
public class JwtService implements JwtUseCase {

    @Inject
    private JwtCommandPort jwtCommandPort;


    @Override
    public boolean validateToken(String jwt) {
        return jwtCommandPort.validateToken(jwt);
    }

    @Override
    public String getSubject(String jwt) throws JwtException {
        return jwtCommandPort.getSubject(jwt);
    }

    @Override
    public String getRole(String jwt) throws JwtException {
        return jwtCommandPort.getRole(jwt);
    }
}
