package pl.lodz.p.it.tks.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import pl.lodz.p.it.tks.dto.user.ClientDTO;
import pl.lodz.p.it.tks.dto.user.UserDTO;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.User;

@ApplicationScoped
public class UserMapper {

    public UserDTO mapToDto(User user) {
        if (user instanceof Client client) {
            return new ClientDTO(client);
        }
        return null;

    }
}
