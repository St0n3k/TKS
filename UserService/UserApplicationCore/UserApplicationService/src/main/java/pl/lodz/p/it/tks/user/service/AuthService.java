package pl.lodz.p.it.tks.user.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import pl.lodz.p.it.tks.user.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.user.exception.user.AuthenticationException;
import pl.lodz.p.it.tks.user.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.user.infrastructure.command.JwtCommandPort;
import pl.lodz.p.it.tks.user.infrastructure.command.UserCommandPort;
import pl.lodz.p.it.tks.user.infrastructure.query.UserQueryPort;
import pl.lodz.p.it.tks.user.model.users.User;
import pl.lodz.p.it.tks.user.ui.AuthUseCase;

import java.security.Principal;
import java.util.Objects;


@RequestScoped
public class AuthService implements AuthUseCase {
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

        return jwtCommandPort.generateJWT(user.getUsername(), user.getRole());
    }

    public void changePassword(String oldPassword, String newPassword)
        throws UserNotFoundException, InvalidInputException {
        Principal principal = securityContext.getUserPrincipal();
        if (principal instanceof User user) {
            if (user.getPassword().equals(oldPassword)
                && !user.getPassword().equals(newPassword)) {
                user.setPassword(newPassword);
                userCommandPort.update(user);
                return;
            }
            throw new InvalidInputException();
        }
        throw new UserNotFoundException();
    }

}
