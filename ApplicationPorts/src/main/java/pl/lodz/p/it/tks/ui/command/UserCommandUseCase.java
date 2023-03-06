package pl.lodz.p.it.tks.ui.command;

import pl.lodz.p.it.tks.exception.user.CreateUserException;
import pl.lodz.p.it.tks.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.Employee;
import pl.lodz.p.it.tks.model.user.User;

public interface UserCommandUseCase {
    Client registerClient(Client client) throws CreateUserException;

    Employee registerEmployee(Employee employee) throws CreateUserException;

    Admin registerAdmin(Admin admin) throws CreateUserException;

    User updateUser(Long id, User dto) throws UserNotFoundException, UpdateUserException;

    User activateUser(Long id) throws UserNotFoundException, UpdateUserException;

    User deactivateUser(Long id) throws UpdateUserException, UserNotFoundException;
}
