package pl.lodz.p.it.tks.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
public abstract class User implements Principal {

    private Long id;

    @NotNull
    private String username;

    @NotNull
    private boolean active = true;

    @NotNull
    private String role = "CLIENT";

    @NotNull
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
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String role, String password) {
        this.username = username;
        this.role = role;
        this.password = password;
    }

    public User(Long id, String username, String role, String password) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.password = password;
    }
}
