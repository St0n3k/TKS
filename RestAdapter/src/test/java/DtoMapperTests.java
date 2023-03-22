import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.dto.rent.RentDTO;
import pl.lodz.p.it.tks.dto.room.ApartmentDTO;
import pl.lodz.p.it.tks.dto.room.RoomDTO;
import pl.lodz.p.it.tks.dto.user.AdminDTO;
import pl.lodz.p.it.tks.dto.user.ClientDTO;
import pl.lodz.p.it.tks.dto.user.EmployeeDTO;
import pl.lodz.p.it.tks.dto.user.UserDTO;
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

public class DtoMapperTests {

    @Test
    void RoomMapperTest() {
        RoomMapper roomMapper = new RoomMapper();

        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 2);
        Apartment apartment = new Apartment(UUID.randomUUID(), 1, 2.0, 3, 2.5, 43);

        RoomDTO roomDTO = roomMapper.mapToDto(room);
        RoomDTO apartmentDto = roomMapper.mapToDto(apartment);

        Assertions.assertTrue(apartmentDto instanceof ApartmentDTO);

        ApartmentDTO apartmentDTO = (ApartmentDTO) apartmentDto;

        Assertions.assertEquals(room.getRoomNumber(), roomDTO.getRoomNumber());
        Assertions.assertEquals(room.getPrice(), roomDTO.getPrice());
        Assertions.assertEquals(room.getSize(), roomDTO.getSize());
        Assertions.assertEquals(room.getVersion(), roomDTO.getVersion());
        Assertions.assertEquals(room.getId(), roomDTO.getId());

        Assertions.assertEquals(apartment.getRoomNumber(), apartmentDTO.getRoomNumber());
        Assertions.assertEquals(apartment.getPrice(), apartmentDTO.getPrice());
        Assertions.assertEquals(apartment.getSize(), apartmentDTO.getSize());
        Assertions.assertEquals(apartment.getBalconyArea(), apartmentDTO.getBalconyArea());
        Assertions.assertEquals(apartment.getVersion(), apartmentDTO.getVersion());
        Assertions.assertEquals(apartment.getId(), apartmentDTO.getId());
    }

    @Test
    void RentMapperTest() {
        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 3);
        Client client = new Client(UUID.randomUUID(), 0, "x", true, "a", "b", "c", "f", new Address("d", "f", 1));
        Rent rent = new Rent(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, 100, client, room, 3);

        RentDTO rentDTO = new RentDTO(rent);

        Assertions.assertEquals(rent.getBeginTime(), rentDTO.getBeginTime());
        Assertions.assertEquals(rent.getEndTime(), rentDTO.getEndTime());
        Assertions.assertEquals(rent.getId(), rentDTO.getId());
        Assertions.assertEquals(rent.getFinalCost(), rentDTO.getFinalCost());
        Assertions.assertEquals(rent.getVersion(), rentDTO.getVersion());
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
