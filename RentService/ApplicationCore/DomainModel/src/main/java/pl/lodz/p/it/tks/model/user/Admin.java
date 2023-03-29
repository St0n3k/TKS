package pl.lodz.p.it.tks.model.user;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }

    public Admin(UUID id, String username, boolean active, String password) {
        super(id, username, active, "ADMIN", password);
    }

    public Admin(UUID id, long version, String username, boolean active, String password) {
        super(id, version, username, active, "ADMIN", password);
    }
}
