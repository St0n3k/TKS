package pl.lodz.p.it.tks.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.user.CreateUserException;
import pl.lodz.p.it.tks.exception.user.UpdateUserException;
import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.infrastructure.RentQueryPort;
import pl.lodz.p.it.tks.infrastructure.UserCommandPort;
import pl.lodz.p.it.tks.infrastructure.UserQueryPort;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.Employee;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.ui.UserUseCase;


@AllArgsConstructor
@NoArgsConstructor
@RequestScoped
public class UserService implements UserUseCase {

    @Inject
    private UserQueryPort userQueryPort;

    @Inject
    private UserCommandPort userCommandPort;

    @Inject
    private RentQueryPort rentQueryPort;


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
    public User getUserById(Long id) throws UserNotFoundException {
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

    public List<Rent> getAllRentsOfClient(Long clientId, Boolean past) throws UserNotFoundException {
        if (userQueryPort.getById(clientId).isEmpty()) {
            throw new UserNotFoundException();
        }
        List<Rent> rents;
        if (past != null) { // find past or active rents
            rents = rentQueryPort.findByClientAndStatus(clientId, past);
        } else { // find all rents
            rents = rentQueryPort.getByClientId(clientId);
        }
        return rents;
    }

    // public User updateUser(Long id, User dto) throws UserNotFoundException, UpdateUserException {
    //     Optional<User> optionalUser = userQueryPort.getById(id);
    //
    //     if (optionalUser.isEmpty()) {
    //         throw new UserNotFoundException();
    //     }
    //     User user = optionalUser.get();
    //
    //     String username = dto.getUsername();
    //     String firstName = dto.getFirstName();
    //     String lastName = dto.getLastName();
    //     String personalId = dto.getPersonalId();
    //     String city = dto.getCity();
    //     String street = dto.getStreet();
    //     Integer number = dto.getNumber();
    //
    //     if (username != null && userRepository.getUserByUsername(username).isPresent()) {
    //         throw new UpdateUserException();
    //     }
    //     User updatedUser;
    //
    //     if (user instanceof Client client) {
    //         client.setUsername(username == null ? client.getUsername() : username);
    //         client.setFirstName(firstName == null ? client.getFirstName() : firstName);
    //         client.setLastName(lastName == null ? client.getLastName() : lastName);
    //         client.setPersonalId(personalId == null ? client.getPersonalId() : personalId);
    //
    //         Address address = client.getAddress();
    //
    //         address.setCity(city == null ? address.getCity() : city);
    //         address.setStreet(street == null ? address.getStreet() : street);
    //         address.setHouseNumber(number == null ? address.getHouseNumber() : number);
    //
    //         updatedUser = client;
    //     } else if (user instanceof Employee employee) {
    //         employee.setUsername(username == null ? employee.getUsername() : username);
    //         employee.setFirstName(firstName == null ? employee.getFirstName() : firstName);
    //         employee.setLastName(lastName == null ? employee.getLastName() : lastName);
    //
    //         updatedUser = employee;
    //     } else if (user instanceof Admin admin) {
    //         admin.setUsername(username == null ? admin.getUsername() : username);
    //
    //         updatedUser = admin;
    //     } else {
    //         throw new UpdateUserException();
    //     }
    //
    //     try {
    //         optionalUser = userRepository.update(updatedUser);
    //     } catch (Exception e) {
    //         throw new UpdateUserException();
    //     }
    //
    //     if (optionalUser.isEmpty()) {
    //         throw new UpdateUserException();
    //     }
    //     user = optionalUser.get();
    //     return user;
    //     // return optionalUser.orElseThrow(UpdateUserException::new);
    // }

    @Override
    public User activateUser(Long id) throws UserNotFoundException, UpdateUserException {
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
    public User deactivateUser(Long id) throws UpdateUserException, UserNotFoundException {
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
