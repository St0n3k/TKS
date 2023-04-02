package pl.lodz.p.it.tks.user.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.user.exception.user.CreateUserException;
import pl.lodz.p.it.tks.user.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.user.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.user.infrastructure.command.UserCommandPort;
import pl.lodz.p.it.tks.user.infrastructure.query.UserQueryPort;
import pl.lodz.p.it.tks.user.model.Address;
import pl.lodz.p.it.tks.user.model.users.Admin;
import pl.lodz.p.it.tks.user.model.users.Client;
import pl.lodz.p.it.tks.user.model.users.Employee;
import pl.lodz.p.it.tks.user.model.users.User;
import pl.lodz.p.it.tks.user.ui.command.UserCommandUseCase;
import pl.lodz.p.it.tks.user.ui.query.UserQueryUseCase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@AllArgsConstructor
@NoArgsConstructor
@RequestScoped
public class UserService implements UserQueryUseCase, UserCommandUseCase {

    @Inject
    private UserQueryPort userQueryPort;

    @Inject
    private UserCommandPort userCommandPort;


    @Override
    public Client registerClient(Client client) throws CreateUserException {
        try {
            client = (Client) userCommandPort.add(client);
        } catch (Exception e) {
            throw new CreateUserException();
        }
        return client;
    }


    @Override
    public Employee registerEmployee(Employee employee) throws CreateUserException {
        employee = (Employee) userCommandPort.add(employee);

        if (employee == null) {
            throw new CreateUserException();
        }
        return employee;
    }

    @Override
    public Admin registerAdmin(Admin admin) throws CreateUserException {

        admin = (Admin) userCommandPort.add(admin);

        if (admin == null) {
            throw new CreateUserException();
        }
        return admin;
    }

    @Override
    public User getUserById(UUID id) throws UserNotFoundException {
        Optional<User> optionalUser = userQueryPort.getById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return optionalUser.get();
    }

    @Override
    public List<User> getAllUsers(String username) {
        List<User> users;
        if (username == null) {
            users = userQueryPort.getAll();
        } else {
            users = userQueryPort.matchUserByUsername(username);
        }
        return users;
    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userQueryPort.getUserByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return optionalUser.get();
    }

    @Override
    public List<Client> getClients(String username) {
        if (username != null) {
            return userQueryPort.getUsersByRoleAndMatchingUsername("CLIENT", username)
                .stream().map(u -> (Client) u)
                .toList();
        }
        return userQueryPort.getUsersByRole("CLIENT")
            .stream()
            .map(user -> (Client) user)
            .collect(Collectors.toList());
    }

    @Override
    public List<Employee> getEmployees() {
        return userQueryPort.getUsersByRole("EMPLOYEE")
            .stream()
            .map(user -> (Employee) user)
            .collect(Collectors.toList());
    }

    @Override
    public List<Admin> getAdmins() {
        return userQueryPort.getUsersByRole("ADMIN")
            .stream()
            .map(user -> (Admin) user)
            .collect(Collectors.toList());
    }

    @Override
    public User updateUser(UUID id, User newUser) throws UserNotFoundException, UpdateUserException {
        User user = userQueryPort.getById(id)
            .orElseThrow(UserNotFoundException::new);

        if (!user.getClass().equals(newUser.getClass())) {
            throw new UpdateUserException();
            // TODO consider changing to different exception
        }

        if (user instanceof Client client) {
            Client newClient = (Client) newUser;

            if (newClient.getFirstName() != null) {
                client.setFirstName(newClient.getFirstName());
            }

            if (newClient.getLastName() != null) {
                client.setLastName(newClient.getLastName());
            }

            // TODO check if personalId is not taken or catch exception from the database
            if (newClient.getPersonalId() != null) {
                client.setPersonalId(newClient.getPersonalId());
            }

            Address address = client.getAddress();
            Address newAddress = newClient.getAddress();

            if (newAddress.getCity() != null) {
                address.setCity(newAddress.getCity());
            }

            if (newAddress.getStreet() != null) {
                address.setStreet(newAddress.getStreet());
            }

            if (newAddress.getHouseNumber() > 0) {
                address.setHouseNumber(newAddress.getHouseNumber());
            }
        } else if (user instanceof Employee employee) {
            Employee newEmployee = (Employee) newUser;

            if (newEmployee.getFirstName() != null) {
                employee.setFirstName(newEmployee.getFirstName());
            }

            if (newEmployee.getLastName() != null) {
                employee.setLastName(newEmployee.getLastName());
            }
        }

        return userCommandPort.update(user)
            .orElseThrow(UpdateUserException::new);
    }

    @Override
    public User activateUser(UUID id) throws UserNotFoundException, UpdateUserException {
        Optional<User> optionalUser = userQueryPort.getById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = optionalUser.get();
        user.setActive(true);

        optionalUser = userCommandPort.update(user);
        if (optionalUser.isEmpty()) {
            throw new UpdateUserException();
        }
        user = optionalUser.get();
        return user;
    }

    @Override
    public User deactivateUser(UUID id) throws UpdateUserException, UserNotFoundException {
        Optional<User> optionalUser = userQueryPort.getById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = optionalUser.get();
        user.setActive(false);

        optionalUser = userCommandPort.update(user);
        if (optionalUser.isEmpty()) {
            throw new UpdateUserException();
        }
        user = optionalUser.get();
        return user;
    }

}
