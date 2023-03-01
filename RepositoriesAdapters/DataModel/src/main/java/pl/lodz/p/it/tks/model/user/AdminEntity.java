package pl.lodz.p.it.tks.model.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Admin")
@NoArgsConstructor
public class AdminEntity extends UserEntity {

    public AdminEntity(long version, Long id, String username, boolean active, String password) {
        super(version, id, username, active, "ADMIN", password);
    }

    public AdminEntity(Long id, String username, boolean active, String password) {
        super(id, username, active, "ADMIN", password);
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
