package pl.lodz.p.it.tks.dtoMapper;

import jakarta.enterprise.context.ApplicationScoped;
import pl.lodz.p.it.tks.dto.AdminDTO;
import pl.lodz.p.it.tks.dto.ClientDTO;
import pl.lodz.p.it.tks.dto.EmployeeDTO;
import pl.lodz.p.it.tks.dto.UserDTO;
import pl.lodz.p.it.tks.exception.shared.ConstructorArgumentException;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.Employee;
import pl.lodz.p.it.tks.model.user.User;

@ApplicationScoped
public class UserMapper {

    public UserDTO mapToDto(User user) {
        if (user instanceof Client client) {
            return new ClientDTO(client);
        } else if (user instanceof Employee employee) {
            return  new EmployeeDTO(employee);
        } else if (user instanceof Admin admin) {
            return  new AdminDTO(admin);
        }
        throw new ConstructorArgumentException();
    }
}
