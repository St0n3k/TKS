package pl.lodz.p.it.tks.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.repository.impl.UserRepository;

import java.util.Collections;


@ApplicationScoped
public class GuesthouseAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private JwtProvider jwtProvider;

    @Inject
    private UserRepository userRepository;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) {
//        String jwt = jwtProvider.getToken(httpServletRequest);
//        if (jwt == null || !jwtProvider.validateToken(jwt)) {
//            return httpMessageContext.notifyContainerAboutLogin("anonymous", new HashSet<>(Collections.singleton("GUEST")));
//        }
//        Claims claims = jwtProvider.parseJWT(jwt).getBody();
//        String subject = claims.getSubject();

//        Optional<User> optionalUser = userRepository.getUserByUsername(subject);
//        if (optionalUser.isEmpty()) {
//            return httpMessageContext.notifyContainerAboutLogin("anonymous", new HashSet<>(Collections.singleton("GUEST")));
//        }
//        String role = claims.get("role", String.class);

//        return httpMessageContext.notifyContainerAboutLogin(optionalUser.get(), Collections.singleton(role));
        //FIXME odkomentowac wyzej i usunac to ponizej
        String role = "ADMIN";
        User fakeUser = new Admin();
        return httpMessageContext.notifyContainerAboutLogin(fakeUser, Collections.singleton(role));
    }
}
