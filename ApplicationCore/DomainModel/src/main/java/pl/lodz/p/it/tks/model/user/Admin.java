package pl.lodz.p.it.tks.model.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Admin extends User {

    public Admin(String username, String password) {
        super(username, "ADMIN", password);
    }

    public Admin(Long id, String username, String password) {
        super(id, username, "ADMIN", password);
    }
}
