package pl.lodz.p.it.tks.ui;

import java.util.List;
import pl.lodz.p.it.tks.exception.user.CreateUserException;
import pl.lodz.p.it.tks.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.Employee;
import pl.lodz.p.it.tks.model.user.User;

public interface UserUseCase {
    Client registerClient(Client client) throws CreateUserException;

    Employee registerEmployee(Employee employee) throws CreateUserException;

    Admin registerAdmin(Admin admin) throws CreateUserException;

    User getUserById(Long id) throws UserNotFoundException;

    List<User> getAllUsers(String username);

    User getUserByUsername(String username) throws UserNotFoundException;

    List<Client> getClients(String username);

    List<Employee> getEmployees();

    List<Admin> getAdmins();

    User activateUser(Long id) throws UserNotFoundException, UpdateUserException;

    User deactivateUser(Long id) throws UpdateUserException, UserNotFoundException;

    List<Rent> getAllRentsOfClient(Long clientId, Boolean past) throws UserNotFoundException;
}
