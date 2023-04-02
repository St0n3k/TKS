package pl.lodz.p.it.tks.user.infrastructure.query;

import pl.lodz.p.it.tks.user.model.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserQueryPort {
    Optional<User> getById(UUID id);

    List<User> getAll();

    Optional<User> getUserByUsername(String username);

    List<User> matchUserByUsername(String phrase);

    List<User> getUsersByRole(String role);

    List<User> getUsersByRoleAndMatchingUsername(String role, String username);
}
