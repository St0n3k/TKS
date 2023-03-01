package pl.lodz.p.it.tks.out;

import java.util.Optional;
import pl.lodz.p.it.tks.model.user.User;

public interface UserCommandPort {
    User add(User user);
    void remove(User user);
    Optional<User> update(User user);
}
