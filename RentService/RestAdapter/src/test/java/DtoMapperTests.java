import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.dto.rent.RentDTO;
import pl.lodz.p.it.tks.dto.room.ApartmentDTO;
import pl.lodz.p.it.tks.dto.room.RoomDTO;
import pl.lodz.p.it.tks.dto.user.ClientDTO;
import pl.lodz.p.it.tks.dto.user.UserDTO;
import pl.lodz.p.it.tks.mapper.RoomMapper;
import pl.lodz.p.it.tks.mapper.UserMapper;
import pl.lodz.p.it.tks.model.Address;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.user.Client;

import java.time.LocalDateTime;
import java.util.UUID;

public class DtoMapperTests {

    @Test
    void roomMapperTest() {
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
    void rentMapperTest() {
        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 3);
        Client client = new Client(UUID.randomUUID(), 0, "x", "b", "c", "f", new Address("d", "f", 1));
        Rent rent =
            new Rent(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, 100, client, room,
                3);

        RentDTO rentDTO = new RentDTO(rent);

        Assertions.assertEquals(rent.getBeginTime(), rentDTO.getBeginTime());
        Assertions.assertEquals(rent.getEndTime(), rentDTO.getEndTime());
        Assertions.assertEquals(rent.getId(), rentDTO.getId());
        Assertions.assertEquals(rent.getFinalCost(), rentDTO.getFinalCost());
        Assertions.assertEquals(rent.getVersion(), rentDTO.getVersion());
    }

    @Test
    void userMapperTest() {
        UserMapper userMapper = new UserMapper();

        Client client = new Client(UUID.randomUUID(), 0, "x", "b", "c", "f", new Address("d", "f", 1));

        UserDTO clientDto = userMapper.mapToDto(client);

        Assertions.assertTrue(clientDto instanceof ClientDTO);

        ClientDTO clientDTO = (ClientDTO) clientDto;

        Assertions.assertEquals(clientDTO.getFirstName(), client.getFirstName());
        Assertions.assertEquals(clientDTO.getLastName(), client.getLastName());
        Assertions.assertEquals(clientDTO.getStreet(), client.getAddress().getStreet());
        Assertions.assertEquals(clientDTO.getCity(), client.getAddress().getCity());
        Assertions.assertEquals(clientDTO.getHouseNumber(), client.getAddress().getHouseNumber());
        Assertions.assertEquals(clientDTO.getUsername(), client.getUsername());
        Assertions.assertEquals(clientDTO.getPersonalId(), client.getPersonalId());
        Assertions.assertEquals(clientDTO.getId(), client.getId());
        Assertions.assertEquals(clientDTO.getVersion(), client.getVersion());
    }

}
