import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.user.model.Address;
import pl.lodz.p.it.tks.user.model.users.Admin;
import pl.lodz.p.it.tks.user.model.users.AdminEntity;
import pl.lodz.p.it.tks.user.model.users.Client;
import pl.lodz.p.it.tks.user.model.users.ClientEntity;
import pl.lodz.p.it.tks.user.model.users.Employee;
import pl.lodz.p.it.tks.user.model.users.EmployeeEntity;

import java.util.UUID;


public class EntityMapperTests {
    @Test
    void userMapperTest() {
        Client client = new Client(UUID.randomUUID(), 0, "x", true, "a", "b", "c", "f", new Address("d", "f", 1));
        Employee employee = new Employee(UUID.randomUUID(), 4, "x", true, "a", "b", "c");
        Admin admin = new Admin(UUID.randomUUID(), 3, "x", true, "a");

        ClientEntity clientEntity = new ClientEntity(client);
        EmployeeEntity employeeEntity = new EmployeeEntity(employee);
        AdminEntity adminEntity = new AdminEntity(admin);


        Assertions.assertEquals(clientEntity.getFirstName(), client.getFirstName());
        Assertions.assertEquals(clientEntity.getLastName(), client.getLastName());
        Assertions.assertEquals(clientEntity.getAddress().getStreet(), client.getAddress().getStreet());
        Assertions.assertEquals(clientEntity.getAddress().getCity(), client.getAddress().getCity());
        Assertions.assertEquals(clientEntity.getAddress().getHouseNumber(), client.getAddress().getHouseNumber());
        Assertions.assertEquals(clientEntity.getRole(), client.getRole());
        Assertions.assertEquals(clientEntity.getUsername(), client.getUsername());
        Assertions.assertEquals(clientEntity.getPersonalId(), client.getPersonalId());
        Assertions.assertEquals(clientEntity.getId(), client.getId());
        Assertions.assertEquals(clientEntity.getVersion(), client.getVersion());

        Assertions.assertEquals(employeeEntity.getRole(), employee.getRole());
        Assertions.assertEquals(employeeEntity.getUsername(), employee.getUsername());
        Assertions.assertEquals(employeeEntity.getFirstName(), employee.getFirstName());
        Assertions.assertEquals(employeeEntity.getLastName(), employee.getLastName());
        Assertions.assertEquals(employeeEntity.getId(), employee.getId());
        Assertions.assertEquals(employeeEntity.getVersion(), employee.getVersion());

        Assertions.assertEquals(adminEntity.getRole(), admin.getRole());
        Assertions.assertEquals(adminEntity.getUsername(), admin.getUsername());
        Assertions.assertEquals(adminEntity.getVersion(), admin.getVersion());
        Assertions.assertEquals(adminEntity.getId(), admin.getId());

        Client retrievedClient = (Client) clientEntity.mapToUser();
        Employee retrievedEmployee = (Employee) employeeEntity.mapToUser();
        Admin retrievedAdmin = (Admin) adminEntity.mapToUser();

        Assertions.assertEquals(client, retrievedClient);
        Assertions.assertEquals(employee, retrievedEmployee);
        Assertions.assertEquals(admin, retrievedAdmin);
    }
}
