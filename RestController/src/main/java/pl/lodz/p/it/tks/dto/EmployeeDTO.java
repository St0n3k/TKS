package pl.lodz.p.it.tks.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.user.Employee;

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
