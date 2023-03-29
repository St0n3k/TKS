package pl.lodz.p.it.tks.model.users;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.AbstractEntity;
import pl.lodz.p.it.tks.model.user.User;

import java.util.UUID;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NamedQueries({
    @NamedQuery(name = "User.getAll",
        query = "SELECT u FROM UserEntity u"),
    @NamedQuery(name = "User.getByUserId",
        query = "SELECT u FROM UserEntity u WHERE u.id = :userId"),
    @NamedQuery(name = "User.getByUsername",
        query = "SELECT u FROM UserEntity u WHERE u.username = :username"),
    @NamedQuery(name = "User.matchByUsername",
        query = "SELECT u FROM UserEntity u WHERE u.username LIKE :username"),
    @NamedQuery(name = "User.getByRole",
        query = "SELECT u FROM UserEntity u WHERE u.role LIKE :role ORDER BY u.id"),
    @NamedQuery(name = "User.getByRoleMatchingName",
        query = """
            SELECT u FROM UserEntity u
            WHERE u.role LIKE :role AND u.username LIKE :username
            ORDER BY u.id""")
})

@Data
@NoArgsConstructor
public abstract class UserEntity extends AbstractEntity {

    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @NotNull
    @Column(name = "active")
    private boolean active = true;

    @NotNull
    @Column(name = "role")
    private String role = "CLIENT";

    @NotNull
    @Column(name = "password")
    private String password;

    public UserEntity(long version, UUID id, String username, boolean active, String role, String password) {
        super(id, version);
        this.username = username;
        this.active = active;
        this.role = role;
        this.password = password;
    }

    public UserEntity(UUID id, String username, boolean active, String role, String password) {
        super(id);
        this.username = username;
        this.active = active;
        this.role = role;
        this.password = password;
    }

    public abstract User mapToUser();
}
