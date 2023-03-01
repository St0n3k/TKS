package pl.lodz.p.it.tks.model.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Employee extends User {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;


    public Employee(String username, String firstName, String lastName, String password) {
        super(username, "EMPLOYEE", password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(Long id, String username, String firstName, String lastName, String password) {
        super(id, username, "EMPLOYEE", password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(Long id,
                    long version,
                    String username,
                    String role,
                    String password,
                    boolean active,
                    String firstName,
                    String lastName) {
        super(id, version, username, role, password, active);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
