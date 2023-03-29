package pl.lodz.p.it.tks.user.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import pl.lodz.p.it.tks.user.dto.user.AdminDTO;
import pl.lodz.p.it.tks.user.dto.user.ClientDTO;
import pl.lodz.p.it.tks.user.dto.user.EmployeeDTO;
import pl.lodz.p.it.tks.user.dto.user.UserDTO;
import pl.lodz.p.it.tks.user.model.users.Admin;
import pl.lodz.p.it.tks.user.model.users.Client;
import pl.lodz.p.it.tks.user.model.users.Employee;
import pl.lodz.p.it.tks.user.model.users.User;

@ApplicationScoped
public class UserMapper {

    public UserDTO mapToDto(User user) {
        if (user instanceof Client client) {
            return new ClientDTO(client);
        } else if (user instanceof Employee employee) {
            return new EmployeeDTO(employee);
        } else {
            return new AdminDTO((Admin) user);
        }
    }
}
