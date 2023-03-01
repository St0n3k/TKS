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

    @Override
    public User mapToUser() {
        return new Admin(this.getId(),
                         this.getUsername(),
                         this.getPassword());
    }
}
