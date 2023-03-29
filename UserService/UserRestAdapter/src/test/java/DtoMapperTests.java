import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.user.dto.user.AdminDTO;
import pl.lodz.p.it.tks.user.dto.user.ClientDTO;
import pl.lodz.p.it.tks.user.dto.user.EmployeeDTO;
import pl.lodz.p.it.tks.user.dto.user.UserDTO;
import pl.lodz.p.it.tks.user.mapper.UserMapper;
import pl.lodz.p.it.tks.user.model.Address;
import pl.lodz.p.it.tks.user.model.users.Admin;
import pl.lodz.p.it.tks.user.model.users.Client;
import pl.lodz.p.it.tks.user.model.users.Employee;

import java.util.UUID;

public class DtoMapperTests {

    @Test
    void userMapperTest() {
        UserMapper userMapper = new UserMapper();

        Client client = new Client(UUID.randomUUID(), 0, "x", true, "a", "b", "c", "f", new Address("d", "f", 1));
        Employee employee = new Employee(UUID.randomUUID(), 4, "x", true, "a", "b", "c");
        Admin admin = new Admin(UUID.randomUUID(), 3, "x", true, "a");

        UserDTO clientDto = userMapper.mapToDto(client);
        UserDTO employeeDto = userMapper.mapToDto(employee);
        UserDTO adminDto = userMapper.mapToDto(admin);

        Assertions.assertTrue(clientDto instanceof ClientDTO);
        Assertions.assertTrue(employeeDto instanceof EmployeeDTO);
        Assertions.assertTrue(adminDto instanceof AdminDTO);

        ClientDTO clientDTO = (ClientDTO) clientDto;
        EmployeeDTO employeeDTO = (EmployeeDTO) employeeDto;
        AdminDTO adminDTO = (AdminDTO) adminDto;


        Assertions.assertEquals(clientDTO.getFirstName(), client.getFirstName());
        Assertions.assertEquals(clientDTO.getLastName(), client.getLastName());
        Assertions.assertEquals(clientDTO.getStreet(), client.getAddress().getStreet());
        Assertions.assertEquals(clientDTO.getCity(), client.getAddress().getCity());
        Assertions.assertEquals(clientDTO.getHouseNumber(), client.getAddress().getHouseNumber());
        Assertions.assertEquals(clientDTO.getRole(), client.getRole());
        Assertions.assertEquals(clientDTO.getUsername(), client.getUsername());
        Assertions.assertEquals(clientDTO.getPersonalId(), client.getPersonalId());
        Assertions.assertEquals(clientDTO.getId(), client.getId());
        Assertions.assertEquals(clientDTO.getVersion(), client.getVersion());

        Assertions.assertEquals(employeeDTO.getRole(), employee.getRole());
        Assertions.assertEquals(employeeDTO.getUsername(), employee.getUsername());
        Assertions.assertEquals(employeeDTO.getFirstName(), employee.getFirstName());
        Assertions.assertEquals(employeeDTO.getLastName(), employee.getLastName());
        Assertions.assertEquals(employeeDTO.getId(), employee.getId());
        Assertions.assertEquals(employeeDTO.getVersion(), employee.getVersion());

        Assertions.assertEquals(adminDTO.getRole(), admin.getRole());
        Assertions.assertEquals(adminDTO.getUsername(), admin.getUsername());
        Assertions.assertEquals(adminDTO.getVersion(), admin.getVersion());
        Assertions.assertEquals(adminDTO.getId(), admin.getId());
    }

}
