package pl.lodz.p.it.tks.user.ui.query;

import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.user.model.users.Admin;
import pl.lodz.p.it.tks.user.model.users.Client;
import pl.lodz.p.it.tks.user.model.users.Employee;
import pl.lodz.p.it.tks.user.model.users.User;

import java.util.List;
import java.util.UUID;

public interface UserQueryUseCase {
    User getUserById(UUID id) throws UserNotFoundException;

    List<User> getAllUsers(String username);

    User getUserByUsername(String username) throws UserNotFoundException;

    List<Client> getClients(String username);

    List<Employee> getEmployees();

    List<Admin> getAdmins();
}
