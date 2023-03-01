package pl.lodz.p.it.tks.aggregates;

import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.tks.in.UserQueryPort;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.AdminEntity;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.ClientEntity;
import pl.lodz.p.it.tks.model.user.Employee;
import pl.lodz.p.it.tks.model.user.EmployeeEntity;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.model.user.UserEntity;
import pl.lodz.p.it.tks.out.UserCommandPort;
import pl.lodz.p.it.tks.repository.UserRepository;

@ApplicationScoped
public class UserRepositoryAdapter implements UserCommandPort, UserQueryPort {

    @Inject
    private UserRepository userRepository;

    //region Query
    @Override
    public Optional<User> getById(Long id) {
        return userRepository.getById(id)
                             .map(UserEntity::mapToUser);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll()
                             .stream()
                             .map(UserEntity::mapToUser)
                             .toList();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username)
                             .map(UserEntity::mapToUser);
    }

    @Override
    public List<User> matchUserByUsername(String phrase) {
        return userRepository.matchUserByUsername(phrase)
                             .stream()
                             .map(UserEntity::mapToUser)
                             .toList();
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userRepository.getUsersByRole(role)
                             .stream()
                             .map(UserEntity::mapToUser)
                             .toList();
    }

    @Override
    public List<User> getUsersByRoleAndMatchingUsername(String role, String username) {
        return userRepository.getUsersByRoleAndMatchingUsername(role, username)
                             .stream()
                             .map(UserEntity::mapToUser)
                             .toList();
    }
    //endregion

    //region Command
    @Override
    public User add(User user) {
        return userRepository.add(mapToUserEntity(user))
                             .mapToUser();
    }

    @Override
    public void remove(User user) {
        userRepository.remove(mapToUserEntity(user));
    }

    @Override
    public Optional<User> update(User user) {
        return userRepository.update(mapToUserEntity(user))
                             .map(UserEntity::mapToUser);
    }
    //endregion

    private UserEntity mapToUserEntity(User user) {
        if (user instanceof Admin admin) {
            // TODO refactor by creating constructor with Admin param
            return new AdminEntity(admin.getVersion(),
                                   admin.getId(),
                                   admin.getUsername(),
                                   admin.isActive(),
                                   admin.getPassword());
        } else if (user instanceof Employee employee) {
            // TODO refactor by creating constructor with Employee param
            return new EmployeeEntity(employee.getVersion(),
                                      employee.getId(),
                                      employee.getUsername(),
                                      employee.isActive(),
                                      employee.getPassword(),
                                      employee.getFirstName(),
                                      employee.getLastName());
        } else if (user instanceof Client client) {
            return new ClientEntity(client);
        }
        return null;
    }
}
