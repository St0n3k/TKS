package pl.lodz.p.it.tks.model.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.User;

import java.util.UUID;

@Entity
@DiscriminatorValue("Admin")
@NoArgsConstructor
public class AdminEntity extends UserEntity {

    public AdminEntity(long version, UUID id, String username, boolean active, String password) {
        super(version, id, username, active, "ADMIN", password);
    }

    public AdminEntity(UUID id, String username, boolean active, String password) {
        super(id, username, active, "ADMIN", password);
    }

    public AdminEntity(Admin admin) {
        super(admin.getVersion(), admin.getId(), admin.getUsername(), admin.isActive(), admin.getRole(),
            admin.getPassword());
    }

    @Override
    public User mapToUser() {
        return new Admin(this.getId(),
            this.getVersion(),
            this.getUsername(),
            this.isActive(),
            this.getPassword());
    }
}
