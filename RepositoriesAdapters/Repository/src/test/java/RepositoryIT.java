import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.lodz.p.it.tks.model.AbstractEntity;
import pl.lodz.p.it.tks.model.AddressEntity;
import pl.lodz.p.it.tks.model.RentEntity;
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.model.user.ClientEntity;
import pl.lodz.p.it.tks.model.user.UserEntity;
import pl.lodz.p.it.tks.repository.RentRepository;
import pl.lodz.p.it.tks.repository.RoomRepository;
import pl.lodz.p.it.tks.repository.UserRepository;
import pl.lodz.p.it.tks.repository.impl.RoomRepositoryImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ArquillianExtension.class)
public class RepositoryIT {

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
//                .addPackages(true, "pl.lodz.p.it.tks")
                .addPackage(RoomRepositoryImpl.class.getPackage())
                .addPackage(RoomRepository.class.getPackage())
                .addPackage(AbstractEntity.class.getPackage())
                .addPackage(UserEntity.class.getPackage())
                .addAsResource("test-initial_data.sql", "META-INF/sql/initial_data.sql")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("test-web.xml", "web.xml")

                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return war;
    }

    @Inject
    private RoomRepository roomRepository;

    @Inject
    private RentRepository rentRepository;

    @Inject
    private UserRepository userRepository;


    //region RoomRepository test
    @Test
    void shouldPassGetRoomByNumber() {
        RoomEntity roomEntity = roomRepository.getByRoomNumber(836)
                .orElseThrow();

        assertEquals(1, roomEntity.getSize());
        assertEquals(836, roomEntity.getRoomNumber());
        assertEquals(707.19, roomEntity.getPrice());
    }

    @Test
    void shouldFailWhenGettingRoomWithNumberThatNotExists() {
        assertThrows(NoSuchElementException.class , () -> roomRepository.getByRoomNumber(-1)
                .orElseThrow());
    }

    @Test
    void shouldPassGetRoomById(){
        RoomEntity roomEntity = roomRepository.
                getById(UUID.fromString("dba673f8-0526-4cea-941e-3c8ddd5e4f92"))
                .orElseThrow();

        assertEquals(1, roomEntity.getSize());
        assertEquals(836, roomEntity.getRoomNumber());
        assertEquals(707.19, roomEntity.getPrice());
    }

    @Test
    void shouldFailWhenGettingNonExistingRoomById(){
        assertThrows(NoSuchElementException.class , () -> roomRepository.
                getById(UUID.fromString("5c3c85ee-c8ad-42e7-b8f9-08f987a91d3e"))
                .orElseThrow());
    }

    @Test
    void shouldPassExistsById(){
        assertTrue(roomRepository.existsById(
                UUID.fromString("dba673f8-0526-4cea-941e-3c8ddd5e4f92"))
        );
    }

    @Test
    void shouldFailByCheckingIfNonExistingRoomExistsById(){
        assertFalse(roomRepository.existsById(
                UUID.fromString("5c3c85ee-c8ad-42e7-b8f9-08f987a91d3e"))
        );
    }

    @Test
    void shouldPassWhenGettingAllRooms(){
        List<RoomEntity> rooms = roomRepository.getAll();
        assertNotNull(rooms);
        assertTrue(rooms.size() > 0);
    }

    @Test
    void shouldPassWhenAddingNewRoom(){
        RoomEntity roomEntity = new RoomEntity(11614, 200.0, 20);
        roomEntity = roomRepository.add(roomEntity);
        RoomEntity checkRoomEntity = roomRepository
                .getById(roomEntity.getId()).orElseThrow();
        assertEquals(roomEntity, checkRoomEntity);
    }

    @Test
    void shouldPassWhenRemovingRoom(){
        RoomEntity roomEntity = new RoomEntity(1615, 200.0, 20);
        roomEntity = roomRepository.add(roomEntity);
        UUID uuid = roomEntity.getId();
        roomRepository.remove(roomEntity);
        assertThrows(NoSuchElementException.class, () -> roomRepository
                .getById(uuid).orElseThrow());
    }

    @Test
    void shouldPassWhenUpdatingRoom(){

        RoomEntity roomEntity = new RoomEntity(16666, 200.0, 20);
        roomEntity = roomRepository.add(roomEntity);

        assertEquals(20, roomEntity.getSize());
        assertEquals(16666, roomEntity.getRoomNumber());
        assertEquals(200.0, roomEntity.getPrice());

        roomEntity.setRoomNumber(1836);
        roomEntity.setPrice(707.10);
        roomEntity.setSize(2);

        RoomEntity checkRoomEntity = roomRepository.update(roomEntity).orElseThrow();
        assertEquals(2, roomEntity.getSize());
        assertEquals(1836, roomEntity.getRoomNumber());
        assertEquals(707.10, roomEntity.getPrice());
    }
    //endregion

    //region RentRepository test
    @Test
    void shouldPassGetRentById() {
        RentEntity rentEntity = rentRepository.getById(UUID.fromString("22208864-7b61-4e6e-8573-53863bd93b35"))
                .orElseThrow();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        assertEquals(LocalDateTime.parse("2023-10-02 11:00", formatter), rentEntity.getBeginTime());
        assertEquals(LocalDateTime.parse("2023-10-05 10:00", formatter), rentEntity.getEndTime());
        assertEquals(1000, rentEntity.getFinalCost());
        assertEquals(UUID.fromString("9acac245-25b3-492d-a742-4c69bfcb90cf"), rentEntity.getRoom().getId());
        assertEquals(UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"), rentEntity.getClient().getId());
    }

    @Test
    void shouldFailWhenGettingNonExistentRent() {
        assertThrows(NoSuchElementException.class , () -> rentRepository
                .getById(UUID.fromString("bdbe2fcf-6203-9999-8908-ca65b9689396"))
                .orElseThrow());
    }

    @Test
    void shouldPassWhenGettingAllRents() {
        List<RentEntity> rents = rentRepository.getAll();
        assertNotNull(rents);
        assertTrue(rents.size() > 0);
    }

    @Test
    void shouldPassWhenGettingAllRentsOfRoom() {
        List<RentEntity> rents = rentRepository.getByRoomId(UUID.fromString("9acac245-25b3-492d-a742-4c69bfcb90cf"));
        assertNotNull(rents);
        assertTrue(rents.size() > 0);
    }

    @Test
    void shouldPassWhenGettingAllRentsOfClient() {
        List<RentEntity> rents = rentRepository.getByClientId(UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"));
        assertNotNull(rents);
        assertTrue(rents.size() > 0);
    }

    @Test
    void shouldReturnEmptyListWhenGettingAllRentsOfNonExistentClient() {
        List<RentEntity> rents = rentRepository.getByClientId(UUID.fromString("9acbc245-6203-47d6-8908-ca65b9689396"));
        assertNotNull(rents);
        assertEquals(0, rents.size());
    }

    @Test
    void shouldReturnEmptyListWhenGettingAllRentsOfNonExistentRoom() {
        List<RentEntity> rents = rentRepository.getByRoomId(UUID.fromString("9aca3245-6203-47d6-8908-ca65b9689396"));
        assertNotNull(rents);
        assertEquals(0, rents.size());
    }

    @Test
    void shouldPassWhenAddingNewRent() {
        RoomEntity roomEntity = new RoomEntity(41614, 200.0, 20);
        AddressEntity addressEntity = new AddressEntity("city", "street", 1);
        ClientEntity clientEntity = new ClientEntity(null, "userndame", true, "password", "Firstname", "Lastname", "164732", addressEntity);

        roomEntity = roomRepository.add(roomEntity);
        clientEntity = (ClientEntity) userRepository.add(clientEntity);

        RentEntity rentEntity = new RentEntity(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 100, clientEntity, roomEntity);

        rentEntity = rentRepository.add(rentEntity);
        RentEntity checkRentEntity = rentRepository
                .getById(rentEntity.getId()).orElseThrow();

        assertEquals(rentEntity, checkRentEntity);
    }

    @Test
    void shouldPassWhenRemovingRent() {
        RoomEntity roomEntity = new RoomEntity(166214, 200.0, 20);
        AddressEntity addressEntity = new AddressEntity("city", "street", 1);
        ClientEntity clientEntity = new ClientEntity(null, "username23", true, "password", "Firstname", "Lastname", "1645732", addressEntity);

        roomEntity = roomRepository.add(roomEntity);
        clientEntity = (ClientEntity) userRepository.add(clientEntity);

        RentEntity rentEntity = new RentEntity(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 100, clientEntity, roomEntity);

        rentEntity = rentRepository.add(rentEntity);

        rentRepository.remove(rentEntity);

        RentEntity finalRentEntity = rentEntity;
        assertThrows(NoSuchElementException.class, () -> rentRepository
                .getById(finalRentEntity.getId()).orElseThrow());
    }

    @Test
    void shouldPassWhenUpdatingRent() {
        RoomEntity roomEntity = roomRepository.getById(UUID.fromString("8378b753-6d05-454b-8447-efb125846fc7"))
                .orElseThrow();

        ClientEntity clientEntity = (ClientEntity) userRepository.getById(UUID.fromString("a524d75e-927a-4a10-8c46-6321fff6979e"))
                .orElseThrow();

        RentEntity rentEntity = new RentEntity(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 100, clientEntity, roomEntity);
        rentEntity = rentRepository.add(rentEntity);

        assertTrue(rentEntity.isBoard());

        rentEntity.setBoard(false);
        rentEntity = rentRepository.update(rentEntity)
                .orElseThrow();

        assertFalse(rentEntity.isBoard());
    }
    //endregion
}
