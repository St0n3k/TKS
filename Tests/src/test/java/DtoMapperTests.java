import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.dto.AdminDTO;
import pl.lodz.p.it.tks.dto.ApartmentDTO;
import pl.lodz.p.it.tks.dto.ClientDTO;
import pl.lodz.p.it.tks.dto.EmployeeDTO;
import pl.lodz.p.it.tks.dto.RentDTO;
import pl.lodz.p.it.tks.dto.RoomDTO;
import pl.lodz.p.it.tks.dto.UserDTO;
import pl.lodz.p.it.tks.dtoMapper.RoomMapper;
import pl.lodz.p.it.tks.dtoMapper.UserMapper;
import pl.lodz.p.it.tks.model.Address;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.user.Admin;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.Employee;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DtoMapperTests {

    @Test
    void RoomMapperTest() {
        RoomMapper roomMapper = new RoomMapper();

        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 2);
        Apartment apartment = new Apartment(UUID.randomUUID(), 1, 2.0, 3, 2.5, 43);

        RoomDTO roomDTO = roomMapper.mapToDto(room);
        RoomDTO apartmentDto = roomMapper.mapToDto(apartment);

        assertTrue(apartmentDto instanceof ApartmentDTO);

        ApartmentDTO apartmentDTO = (ApartmentDTO) apartmentDto;

        assertEquals(room.getRoomNumber(), roomDTO.getRoomNumber());
        assertEquals(room.getPrice(), roomDTO.getPrice());
        assertEquals(room.getSize(), roomDTO.getSize());
        assertEquals(room.getVersion(), roomDTO.getVersion());
        assertEquals(room.getId(), roomDTO.getId());

        assertEquals(apartment.getRoomNumber(), apartmentDTO.getRoomNumber());
        assertEquals(apartment.getPrice(), apartmentDTO.getPrice());
        assertEquals(apartment.getSize(), apartmentDTO.getSize());
        assertEquals(apartment.getBalconyArea(), apartmentDTO.getBalconyArea());
        assertEquals(apartment.getVersion(), apartmentDTO.getVersion());
        assertEquals(apartment.getId(), apartmentDTO.getId());
    }

    @Test
    void RentMapperTest() {
        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 3);
        Client client = new Client(UUID.randomUUID(), 0, "x", true, "a", "b", "c", "f", new Address("d", "f", 1));
        Rent rent = new Rent(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, 100, client, room, 3);

        RentDTO rentDTO = new RentDTO(rent);

        assertEquals(rent.getBeginTime(), rentDTO.getBeginTime());
        assertEquals(rent.getEndTime(), rentDTO.getEndTime());
        assertEquals(rent.getId(), rentDTO.getId());
        assertEquals(rent.getFinalCost(), rentDTO.getFinalCost());
        assertEquals(rent.getVersion(), rentDTO.getVersion());
    }

    @Test
    void UserMapperTest() {
        UserMapper userMapper = new UserMapper();

        Client client = new Client(UUID.randomUUID(), 0, "x", true, "a", "b", "c", "f", new Address("d", "f", 1));
        Employee employee = new Employee(UUID.randomUUID(), 4, "x", true, "a", "b", "c");
        Admin admin = new Admin(UUID.randomUUID(), 3, "x", true, "a");

        UserDTO clientDto = userMapper.mapToDto(client);
        UserDTO employeeDto = userMapper.mapToDto(employee);
        UserDTO adminDto = userMapper.mapToDto(admin);

        assertTrue(clientDto instanceof ClientDTO);
        assertTrue(employeeDto instanceof EmployeeDTO);
        assertTrue(adminDto instanceof AdminDTO);

        ClientDTO clientDTO = (ClientDTO) clientDto;
        EmployeeDTO employeeDTO = (EmployeeDTO) employeeDto;
        AdminDTO adminDTO = (AdminDTO) adminDto;


        assertEquals(clientDTO.getFirstName(), client.getFirstName());
        assertEquals(clientDTO.getLastName(), client.getLastName());
        assertEquals(clientDTO.getStreet(), client.getAddress().getStreet());
        assertEquals(clientDTO.getCity(), client.getAddress().getCity());
        assertEquals(clientDTO.getHouseNumber(), client.getAddress().getHouseNumber());
        assertEquals(clientDTO.getRole(), client.getRole());
        assertEquals(clientDTO.getUsername(), client.getUsername());
        assertEquals(clientDTO.getPersonalId(), client.getPersonalId());
        assertEquals(clientDTO.getId(), client.getId());
        assertEquals(clientDTO.getVersion(), client.getVersion());

        assertEquals(employeeDTO.getRole(), employee.getRole());
        assertEquals(employeeDTO.getUsername(), employee.getUsername());
        assertEquals(employeeDTO.getFirstName(), employee.getFirstName());
        assertEquals(employeeDTO.getLastName(), employee.getLastName());
        assertEquals(employeeDTO.getId(), employee.getId());
        assertEquals(employeeDTO.getVersion(), employee.getVersion());

        assertEquals(adminDTO.getRole(), admin.getRole());
        assertEquals(adminDTO.getUsername(), admin.getUsername());
        assertEquals(adminDTO.getVersion(), admin.getVersion());
        assertEquals(adminDTO.getId(), admin.getId());
    }

}
