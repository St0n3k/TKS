package pl.lodz.p.it.tks.user.ui.command;

import pl.lodz.p.it.tks.user.exception.user.CreateUserException;
import pl.lodz.p.it.tks.user.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.user.model.users.Admin;
import pl.lodz.p.it.tks.user.model.users.Client;
import pl.lodz.p.it.tks.user.model.users.Employee;
import pl.lodz.p.it.tks.user.model.users.User;

import java.util.UUID;

public interface UserCommandUseCase {
    Client registerClient(Client client) throws CreateUserException;

    Employee registerEmployee(Employee employee) throws CreateUserException;

    Admin registerAdmin(Admin admin) throws CreateUserException;

    User updateUser(UUID id, User dto) throws UserNotFoundException, UpdateUserException;

    User activateUser(UUID id) throws UserNotFoundException, UpdateUserException;

    User deactivateUser(UUID id) throws UpdateUserException, UserNotFoundException;

    void deleteUser(UUID id) throws UserNotFoundException;
}
