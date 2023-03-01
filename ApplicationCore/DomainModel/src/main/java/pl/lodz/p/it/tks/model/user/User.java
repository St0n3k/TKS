package pl.lodz.p.it.tks.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.json.bind.annotation.JsonbTransient;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.AbstractModel;

import java.security.Principal;

@Data
@NoArgsConstructor
public abstract class User extends AbstractModel implements Principal {


    private String username;

    private boolean active = true;

    private String role = "CLIENT";

    @JsonbTransient
    private String password;

    @Override
    @JsonIgnore
    public String getName() {
        return getUsername();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }

    public User(String username, String role, String password) {
        this.username = username;
        this.role = role;
        this.password = password;
    }

    public User(Long id, String username, String role, String password) {
        super(id);
        this.username = username;
        this.role = role;
        this.password = password;
    }

    public User(Long id, long version, String username, String role, String password) {
        super(id, version);
        this.username = username;
        this.role = role;
        this.password = password;
    }

    public User(Long id, long version, String username, String role, String password, boolean active) {
        this(id, version, username, role, password);
        this.active = active;
    }
}
