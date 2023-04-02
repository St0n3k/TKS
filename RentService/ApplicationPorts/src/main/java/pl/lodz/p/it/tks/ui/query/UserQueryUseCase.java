package pl.lodz.p.it.tks.ui.query;

import pl.lodz.p.it.tks.exception.user.UserNotFoundException;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.User;

import java.util.List;
import java.util.UUID;

public interface UserQueryUseCase {
    User getUserById(UUID id) throws UserNotFoundException;

    List<User> getAllUsers(String username);

    User getUserByUsername(String username) throws UserNotFoundException;

    List<Client> getClients(String username);

    List<Rent> getAllRentsOfClient(UUID clientId, Boolean past) throws UserNotFoundException;
}
