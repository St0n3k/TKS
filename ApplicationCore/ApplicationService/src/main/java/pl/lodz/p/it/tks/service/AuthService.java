package pl.lodz.p.it.tks.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import pl.lodz.p.it.tks.exception.InvalidInputException;
import pl.lodz.p.it.tks.exception.user.AuthenticationException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.in.UserQueryPort;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.out.JwtCommandPort;
import pl.lodz.p.it.tks.out.UserCommandPort;

import java.security.Principal;
import java.util.Objects;


@RequestScoped
public class AuthService {
    @Inject
    private JwtCommandPort jwtCommandPort;

    @Context
    private SecurityContext securityContext;

    @Inject
    private UserQueryPort userQueryPort;

    @Inject
    private UserCommandPort userCommandPort;

    public String login(String username, String password)
            throws AuthenticationException, InactiveUserException {
        User user = userQueryPort.getUserByUsername(username)
                .orElseThrow(AuthenticationException::new);

        if (!Objects.equals(user.getPassword(), password)) {
            throw new AuthenticationException();
        }

        if (!user.isActive()) {
            throw new InactiveUserException();
        }

        String jwt = jwtCommandPort.generateJWT(user.getUsername(), user.getRole());
        return jwt;
    }

    public void changePassword(String oldPassword, String newPassword) throws UserNotFoundException, InvalidInputException {
        Principal principal = securityContext.getUserPrincipal();
        if (principal instanceof User user) {
            if(user.getPassword().equals(oldPassword)
                    && !user.getPassword().equals(newPassword)){
                user.setPassword(newPassword);
                //FIXME odkomentowac jak beda command porty
//                userCommandPort.update(user);
                return;
            }
            throw new InvalidInputException();
        }
        throw new UserNotFoundException();
    }

}
