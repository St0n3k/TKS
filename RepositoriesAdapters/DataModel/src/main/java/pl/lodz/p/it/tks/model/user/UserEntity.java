package pl.lodz.p.it.tks.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.AbstractEntity;

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

    @Id
    @GeneratedValue(generator = "userId", strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

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

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserEntity(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserEntity(Long id, String username, String password, long version) {
        super(version);
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserEntity(long version, Long id, String username, boolean active, String password) {
        super(version);
        this.id = id;
        this.username = username;
        this.active = active;
        this.password = password;
    }

    public abstract User mapToUser();
}
