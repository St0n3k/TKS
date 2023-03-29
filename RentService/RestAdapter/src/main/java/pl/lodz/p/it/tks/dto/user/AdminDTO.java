package pl.lodz.p.it.tks.dto.user;

import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.user.Admin;

@NoArgsConstructor
public class AdminDTO extends UserDTO {

    public AdminDTO(Admin admin) {
        super(admin);
    }
}
