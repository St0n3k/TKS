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
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.setRole("EMPLOYEE");
    }
}
