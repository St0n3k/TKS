package pl.lodz.p.it.tks.in;

import java.util.List;
import java.util.Optional;
import pl.lodz.p.it.tks.model.user.User;

public interface UserQueryPort {
    Optional<User> getById(Long id);
    List<User> getAll();
    Optional<User> getUserByUsername(String username);
    List<User> matchUserByUsername(String phrase);
    List<User> getUsersByRole(String role);
    List<User> getUsersByRoleAndMatchingUsername(String role, String username);
}
