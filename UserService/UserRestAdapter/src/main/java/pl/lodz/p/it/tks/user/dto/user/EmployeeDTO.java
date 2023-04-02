package pl.lodz.p.it.tks.user.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.user.model.users.Employee;

@Data
@NoArgsConstructor
public class EmployeeDTO extends UserDTO {

    private String firstName;

    private String lastName;


    public EmployeeDTO(Employee employee) {
        super(employee);
        firstName = employee.getFirstName();
        lastName = employee.getLastName();
    }
}
