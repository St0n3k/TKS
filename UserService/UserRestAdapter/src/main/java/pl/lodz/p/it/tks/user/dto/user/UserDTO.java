package pl.lodz.p.it.tks.user.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.user.dto.AbstractDTO;
import pl.lodz.p.it.tks.user.model.users.User;

@Data
@NoArgsConstructor
public abstract class UserDTO extends AbstractDTO {

    private String username;

    private boolean active = true;

    private String role = "CLIENT";


    public UserDTO(User user) {
        super(user.getId(), user.getVersion());
        username = user.getUsername();
        active = user.isActive();
        role = user.getRole();
    }
}
