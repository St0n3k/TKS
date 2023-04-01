package pl.lodz.p.it.tks.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.AbstractModel;

import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class User extends AbstractModel {

    private String username;

    public User(String username) {
        this.username = username;
    }

    public User(UUID id, String username) {
        super(id);
        this.username = username;
    }

    public User(UUID id, long version, String username) {
        super(id, version);
        this.username = username;
    }
}
