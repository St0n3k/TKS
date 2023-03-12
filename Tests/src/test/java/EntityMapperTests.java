import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.model.Address;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.ApartmentEntity;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.RentEntity;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.AdminEntity;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.ClientEntity;
import pl.lodz.p.it.tks.model.user.Employee;
import pl.lodz.p.it.tks.model.user.EmployeeEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class EntityMapperTests {

    @Test
    void RoomMapperTest() {
        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 2);
        Apartment apartment = new Apartment(UUID.randomUUID(), 1, 2.0, 3, 2.5, 43);

        RoomEntity roomEntity = new RoomEntity(room);
        ApartmentEntity apartmentEntity = new ApartmentEntity(apartment);

        assertEquals(room.getRoomNumber(), roomEntity.getRoomNumber());
        assertEquals(room.getPrice(), roomEntity.getPrice());
        assertEquals(room.getSize(), roomEntity.getSize());
        assertEquals(room.getVersion(), roomEntity.getVersion());
        assertEquals(room.getId(), roomEntity.getId());

        assertEquals(apartment.getRoomNumber(), apartmentEntity.getRoomNumber());
        assertEquals(apartment.getPrice(), apartmentEntity.getPrice());
        assertEquals(apartment.getSize(), apartmentEntity.getSize());
        assertEquals(apartment.getBalconyArea(), apartmentEntity.getBalconyArea());
        assertEquals(apartment.getVersion(), apartmentEntity.getVersion());
        assertEquals(apartment.getId(), apartmentEntity.getId());

        Room retrievedRoom = roomEntity.mapToRoom();
        Room retrievedApartment = apartmentEntity.mapToRoom();

        assertEquals(room, retrievedRoom);
        assertEquals(apartment, retrievedApartment);
    }

    @Test
    void RentMapperTest() {
        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 3);
        Client client = new Client(UUID.randomUUID(), 0, "x", true, "a", "b", "c", "f", new Address("d", "f", 1));
        Rent rent = new Rent(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, 100, client, room, 3);

        RentEntity rentEntity = new RentEntity(rent);

        assertEquals(rent.getBeginTime(), rentEntity.getBeginTime());
        assertEquals(rent.getEndTime(), rentEntity.getEndTime());
        assertEquals(rent.getId(), rentEntity.getId());
        assertEquals(rent.getFinalCost(), rentEntity.getFinalCost());
        assertEquals(rent.getVersion(), rentEntity.getVersion());

        Rent retrievedRent = rentEntity.mapToRent();

        assertEquals(rent, retrievedRent);
    }

    @Test
    void UserMapperTest() {
        Client client = new Client(UUID.randomUUID(), 0, "x", true, "a", "b", "c", "f", new Address("d", "f", 1));
        Employee employee = new Employee(UUID.randomUUID(), 4, "x", true, "a", "b", "c");
        Admin admin = new Admin(UUID.randomUUID(), 3, "x", true, "a");

        ClientEntity clientEntity = new ClientEntity(client);
        EmployeeEntity employeeEntity = new EmployeeEntity(employee);
        AdminEntity adminEntity = new AdminEntity(admin);


        assertEquals(clientEntity.getFirstName(), client.getFirstName());
        assertEquals(clientEntity.getLastName(), client.getLastName());
        assertEquals(clientEntity.getAddress().getStreet(), client.getAddress().getStreet());
        assertEquals(clientEntity.getAddress().getCity(), client.getAddress().getCity());
        assertEquals(clientEntity.getAddress().getHouseNumber(), client.getAddress().getHouseNumber());
        assertEquals(clientEntity.getRole(), client.getRole());
        assertEquals(clientEntity.getUsername(), client.getUsername());
        assertEquals(clientEntity.getPersonalId(), client.getPersonalId());
        assertEquals(clientEntity.getId(), client.getId());
        assertEquals(clientEntity.getVersion(), client.getVersion());

        assertEquals(employeeEntity.getRole(), employee.getRole());
        assertEquals(employeeEntity.getUsername(), employee.getUsername());
        assertEquals(employeeEntity.getFirstName(), employee.getFirstName());
        assertEquals(employeeEntity.getLastName(), employee.getLastName());
        assertEquals(employeeEntity.getId(), employee.getId());
        assertEquals(employeeEntity.getVersion(), employee.getVersion());

        assertEquals(adminEntity.getRole(), admin.getRole());
        assertEquals(adminEntity.getUsername(), admin.getUsername());
        assertEquals(adminEntity.getVersion(), admin.getVersion());
        assertEquals(adminEntity.getId(), admin.getId());

        Client retrievedClient = (Client) clientEntity.mapToUser();
        Employee retrievedEmployee = (Employee) employeeEntity.mapToUser();
        Admin retrievedAdmin = (Admin) adminEntity.mapToUser();

        assertEquals(client, retrievedClient);
        assertEquals(employee, retrievedEmployee);
        assertEquals(admin, retrievedAdmin);
    }
}
