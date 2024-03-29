import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.model.Address;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.ApartmentEntity;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.RentEntity;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.model.user.Client;
import pl.lodz.p.it.tks.model.user.ClientEntity;

import java.time.LocalDateTime;
import java.util.UUID;


public class EntityMapperTests {

    @Test
    void roomMapperTest() {
        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 2);
        Apartment apartment = new Apartment(UUID.randomUUID(), 1, 2.0, 3, 2.5, 43);

        RoomEntity roomEntity = new RoomEntity(room);
        ApartmentEntity apartmentEntity = new ApartmentEntity(apartment);

        Assertions.assertEquals(room.getRoomNumber(), roomEntity.getRoomNumber());
        Assertions.assertEquals(room.getPrice(), roomEntity.getPrice());
        Assertions.assertEquals(room.getSize(), roomEntity.getSize());
        Assertions.assertEquals(room.getVersion(), roomEntity.getVersion());
        Assertions.assertEquals(room.getId(), roomEntity.getId());

        Assertions.assertEquals(apartment.getRoomNumber(), apartmentEntity.getRoomNumber());
        Assertions.assertEquals(apartment.getPrice(), apartmentEntity.getPrice());
        Assertions.assertEquals(apartment.getSize(), apartmentEntity.getSize());
        Assertions.assertEquals(apartment.getBalconyArea(), apartmentEntity.getBalconyArea());
        Assertions.assertEquals(apartment.getVersion(), apartmentEntity.getVersion());
        Assertions.assertEquals(apartment.getId(), apartmentEntity.getId());

        Room retrievedRoom = roomEntity.mapToRoom();
        Room retrievedApartment = apartmentEntity.mapToRoom();

        Assertions.assertEquals(room, retrievedRoom);
        Assertions.assertEquals(apartment, retrievedApartment);
    }

    @Test
    void rentMapperTest() {
        Room room = new Room(UUID.randomUUID(), 1, 2.0, 3, 3);
        Client client = new Client(UUID.randomUUID(), 0, "x","b", "c", "f", new Address("d", "f", 1));
        Rent rent =
            new Rent(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, 100, client, room,
                3);

        RentEntity rentEntity = new RentEntity(rent);

        Assertions.assertEquals(rent.getBeginTime(), rentEntity.getBeginTime());
        Assertions.assertEquals(rent.getEndTime(), rentEntity.getEndTime());
        Assertions.assertEquals(rent.getId(), rentEntity.getId());
        Assertions.assertEquals(rent.getFinalCost(), rentEntity.getFinalCost());
        Assertions.assertEquals(rent.getVersion(), rentEntity.getVersion());

        Rent retrievedRent = rentEntity.mapToRent();

        Assertions.assertEquals(rent, retrievedRent);
    }

    @Test
    void userMapperTest() {
        Client client = new Client(UUID.randomUUID(), 0, "x", "b", "c", "f", new Address("d", "f", 1));

        ClientEntity clientEntity = new ClientEntity(client);


        Assertions.assertEquals(clientEntity.getFirstName(), client.getFirstName());
        Assertions.assertEquals(clientEntity.getLastName(), client.getLastName());
        Assertions.assertEquals(clientEntity.getAddress().getStreet(), client.getAddress().getStreet());
        Assertions.assertEquals(clientEntity.getAddress().getCity(), client.getAddress().getCity());
        Assertions.assertEquals(clientEntity.getAddress().getHouseNumber(), client.getAddress().getHouseNumber());
        Assertions.assertEquals(clientEntity.getUsername(), client.getUsername());
        Assertions.assertEquals(clientEntity.getPersonalId(), client.getPersonalId());
        Assertions.assertEquals(clientEntity.getId(), client.getId());
        Assertions.assertEquals(clientEntity.getVersion(), client.getVersion());
    }
}
