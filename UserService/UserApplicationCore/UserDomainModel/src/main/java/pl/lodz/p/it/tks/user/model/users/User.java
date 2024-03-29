package pl.lodz.p.it.tks.user.model.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.user.model.AbstractModel;

import java.security.Principal;
import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class User extends AbstractModel implements Principal {

    private String username;

    private boolean active = true;

    private String role = "CLIENT";

    private String password;

    @Override
    public String getName() {
        return getUsername();
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(UUID id, String username, boolean active, String role, String password) {
        super(id);
        this.username = username;
        this.active = active;
        this.role = role;
        this.password = password;
    }

    public User(UUID id, long version, String username, boolean active, String role, String password) {
        super(id, version);
        this.username = username;
        this.active = active;
        this.role = role;
        this.password = password;
    }
}
