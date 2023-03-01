package pl.lodz.p.it.tks.model.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Admin")
@NoArgsConstructor
public class AdminEntity extends UserEntity {

    public AdminEntity(String username, String password) {
        super(username, password);
        this.setRole("ADMIN");
    }

    public AdminEntity(Long id, String username, String password, long version) {
        super(id, username, password, version);
    }

    public AdminEntity(long version, Long id, String username, boolean active, String password) {
        super(version, id, username, active, password);
    }

    @Override
    public User mapToUser() {
        return new Admin(this.getId(),
                         this.getVersion(),
                         this.getUsername(),
                         this.getRole(),
                         this.getPassword(),
                         this.isActive());
    }
}
