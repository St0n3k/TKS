package pl.lodz.p.it.tks.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

    public EmployeeEntity(long version,
                          UUID id,
                          String username,
                          boolean active,
                          String password,
                          String firstName,
                          String lastName) {
        super(version, id, username, active, "EMPLOYEE", password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public EmployeeEntity(UUID id,
                          String username,
                          boolean active,
                          String password,
                          String firstName,
                          String lastName) {
        super(id, username, active, "EMPLOYEE", password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public EmployeeEntity(Employee employee) {
        super(employee.getVersion(), employee.getId(), employee.getUsername(), employee.isActive(), employee.getRole(),
            employee.getPassword());
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();

    }

    @Override
    public User mapToUser() {
        return new Employee(this.getId(),
            this.getVersion(),
            this.getUsername(),
            this.isActive(),
            this.getPassword(),
            this.firstName,
            this.lastName);
    }
}
