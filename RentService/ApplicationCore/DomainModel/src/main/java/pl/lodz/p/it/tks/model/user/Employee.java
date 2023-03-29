package pl.lodz.p.it.tks.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Employee extends User {

    private String firstName;

    private String lastName;

    public Employee(String username, String firstName, String lastName, String password) {
        super(username, password, "EMPLOYEE");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(UUID id,
                    String username,
                    boolean active,
                    String password,
                    String firstName,
                    String lastName) {
        super(id, username, active, "EMPLOYEE", password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(String username, String firstName, String lastName) {
        super(username, null, "EMPLOYEE");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(UUID id,
                    long version,
                    String username,
                    boolean active,
                    String password,
                    String firstName,
                    String lastName) {
        super(id, version, username, active, "EMPLOYEE", password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(String firstName, String lastName) {
        this(null, firstName, lastName, null);
    }
}
