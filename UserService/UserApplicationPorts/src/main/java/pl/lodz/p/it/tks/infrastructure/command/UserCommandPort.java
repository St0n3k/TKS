package pl.lodz.p.it.tks.infrastructure.command;

import pl.lodz.p.it.tks.model.user.User;

import java.util.Optional;

public interface UserCommandPort {
    User add(User user);

    void remove(User user);

    Optional<User> update(User user);
}
