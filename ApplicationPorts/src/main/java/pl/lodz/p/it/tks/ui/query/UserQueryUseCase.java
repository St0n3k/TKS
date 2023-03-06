package pl.lodz.p.it.tks.ui.query;

import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.Employee;
import pl.lodz.p.it.tks.model.user.User;

import java.util.List;

public interface UserQueryUseCase {
    User getUserById(Long id) throws UserNotFoundException;

    List<User> getAllUsers(String username);

    User getUserByUsername(String username) throws UserNotFoundException;

    List<Client> getClients(String username);

    List<Employee> getEmployees();

    List<Admin> getAdmins();

    List<Rent> getAllRentsOfClient(Long clientId, Boolean past) throws UserNotFoundException;
}
