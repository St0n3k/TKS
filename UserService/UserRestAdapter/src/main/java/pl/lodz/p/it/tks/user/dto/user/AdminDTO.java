package pl.lodz.p.it.tks.user.dto.user;

import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.user.model.users.Admin;

@NoArgsConstructor
public class AdminDTO extends UserDTO {

    public AdminDTO(Admin admin) {
        super(admin);
    }
}
