package pl.lodz.p.it.tks.repository;

import pl.lodz.p.it.tks.model.users.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<UserEntity> {
    Optional<UserEntity> getUserByUsername(String username);

    List<UserEntity> matchUserByUsername(String phrase);

    List<UserEntity> getUsersByRole(String role);

    List<UserEntity> getAllUsers();

    List<UserEntity> getUsersByRoleAndMatchingUsername(String role, String username);
}
