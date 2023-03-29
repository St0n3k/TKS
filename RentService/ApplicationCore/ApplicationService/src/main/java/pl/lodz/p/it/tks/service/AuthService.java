package pl.lodz.p.it.tks.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.exception.user.AuthenticationException;
import pl.lodz.p.it.tks.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.infrastructure.command.JwtCommandPort;
import pl.lodz.p.it.tks.infrastructure.command.UserCommandPort;
import pl.lodz.p.it.tks.infrastructure.query.UserQueryPort;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.ui.AuthUseCase;

import java.security.Principal;


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

        //        if (!Objects.equals(user.getPassword(), password)) {
        //            throw new AuthenticationException();
        //        }
        //
        //        if (!user.isActive()) {
        //            throw new InactiveUserException();
        //        }

        //        return jwtCommandPort.generateJWT(user.getUsername(), user.getRole());
        //FIXME
        return jwtCommandPort.generateJWT(user.getUsername(), "ADMIN");
    }

    public void changePassword(String oldPassword, String newPassword)
        throws UserNotFoundException, InvalidInputException {
        Principal principal = securityContext.getUserPrincipal();
        if (principal instanceof User user) {
            //            if (user.getPassword().equals(oldPassword)
            //                && !user.getPassword().equals(newPassword)) {
            //                user.setPassword(newPassword);
            //                userCommandPort.update(user);
            //                return;
            //
            //            }
            //            throw new InvalidInputException();
            //FIXME
            return;
        }
        throw new UserNotFoundException();
    }

}
