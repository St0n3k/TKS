package pl.lodz.p.it.tks.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.lodz.p.it.tks.exception.security.JwtException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.ui.JwtUseCase;
import pl.lodz.p.it.tks.ui.query.UserQueryUseCase;

import java.util.Collections;
import java.util.HashSet;


@ApplicationScoped
public class GuesthouseAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private JwtUseCase jwtUseCase;

    @Inject
    private UserQueryUseCase userQueryUseCase;

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

        User user;
        try {
            user = userQueryUseCase.getUserByUsername(subject);
        } catch (UserNotFoundException e) {
            return loginAnonymous(httpMessageContext);
        }

        return httpMessageContext.notifyContainerAboutLogin(user, Collections.singleton(role));
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
