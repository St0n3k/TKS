package pl.lodz.p.it.tks.ui.command;

import pl.lodz.p.it.tks.exception.user.CreateUserException;
import pl.lodz.p.it.tks.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.User;

import java.util.UUID;

public interface UserCommandUseCase {
    Client registerClient(Client client) throws CreateUserException;

    User updateUser(UUID id, User dto) throws UserNotFoundException, UpdateUserException;

    //TODO
    //    User activateUser(UUID id) throws UserNotFoundException, UpdateUserException;
    //
    //    User deactivateUser(UUID id) throws UpdateUserException, UserNotFoundException;
}
