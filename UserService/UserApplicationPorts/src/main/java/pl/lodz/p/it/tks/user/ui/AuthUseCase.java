package pl.lodz.p.it.tks.user.ui;

import pl.lodz.p.it.tks.user.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.user.exception.user.AuthenticationException;
import pl.lodz.p.it.tks.user.exception.user.InactiveUserException;
import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;

public interface AuthUseCase {
    String login(String username, String password) throws AuthenticationException, InactiveUserException;

    void changePassword(String oldPassword, String newPassword) throws UserNotFoundException, InvalidInputException;
}
