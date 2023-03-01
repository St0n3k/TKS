package pl.lodz.p.it.tks.aggregates;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.tks.in.UserQueryPort;
import pl.lodz.p.it.tks.model.user.User;
import pl.lodz.p.it.tks.model.user.UserEntity;
import pl.lodz.p.it.tks.out.UserCommandPort;
import pl.lodz.p.it.tks.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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

}
