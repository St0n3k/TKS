package pl.lodz.p.it.tks.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Employee")
@Data
@NoArgsConstructor
public class EmployeeEntity extends UserEntity {

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;


    public EmployeeEntity(String username, String firstName, String lastName, String password) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.setRole("EMPLOYEE");
    }
}
