package pl.lodz.p.it.tks.user.infrastructure.command;

import pl.lodz.p.it.tks.user.model.users.User;

import java.util.Optional;

public interface UserCommandPort {
    User add(User user);

    void remove(User user);

    Optional<User> update(User user);
}
