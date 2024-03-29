package pl.lodz.p.it.tks.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.lodz.p.it.tks.exception.security.JwtException;
import pl.lodz.p.it.tks.ui.JwtUseCase;

import java.util.Collections;
import java.util.HashSet;


@ApplicationScoped
public class GuesthouseAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private JwtUseCase jwtUseCase;


    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                HttpMessageContext httpMessageContext) {
        String jwt = getToken(httpServletRequest);
        if (jwt == null || !jwtUseCase.validateToken(jwt)) {
            return loginAnonymous(httpMessageContext);
        }
        String subject;
        String role;
        try {
            subject = jwtUseCase.getSubject(jwt);
            role = jwtUseCase.getRole(jwt);
        } catch (JwtException e) {
            return loginAnonymous(httpMessageContext);
        }

        return httpMessageContext.notifyContainerAboutLogin(subject, Collections.singleton(role));
    }

    private AuthenticationStatus loginAnonymous(HttpMessageContext httpMessageContext) {
        return httpMessageContext.notifyContainerAboutLogin("anonymous", new HashSet<>(Collections.singleton("GUEST")));
    }

    private String getToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}
