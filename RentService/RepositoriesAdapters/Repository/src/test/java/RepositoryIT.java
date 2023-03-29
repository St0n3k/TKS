import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.lodz.p.it.tks.model.AbstractEntity;
import pl.lodz.p.it.tks.model.AddressEntity;
import pl.lodz.p.it.tks.model.RentEntity;
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.model.users.AdminEntity;
import pl.lodz.p.it.tks.model.users.ClientEntity;
import pl.lodz.p.it.tks.model.users.EmployeeEntity;
import pl.lodz.p.it.tks.model.users.UserEntity;
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
@TestMethodOrder(OrderAnnotation.class)
public class RepositoryIT {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            // .addPackages(true, "pl.lodz.p.it.tks")
            .addPackage(RoomRepositoryImpl.class.getPackage())
            .addPackage(RoomRepository.class.getPackage())
            .addPackage(AbstractEntity.class.getPackage())
            .addPackage(UserEntity.class.getPackage())
            .addAsResource("test-initial_data.sql", "META-INF/sql/initial_data.sql")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("test-web.xml", "web.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
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
        assertThrows(NoSuchElementException.class, () -> roomRepository.getByRoomNumber(-1)
            .orElseThrow());
    }

    @Test
    void shouldPassGetRoomById() {
        RoomEntity roomEntity = roomRepository
            .getById(UUID.fromString("dba673f8-0526-4cea-941e-3c8ddd5e4f92"))
            .orElseThrow();

        assertEquals(1, roomEntity.getSize());
        assertEquals(836, roomEntity.getRoomNumber());
        assertEquals(707.19, roomEntity.getPrice());
    }

    @Test
    void shouldFailWhenGettingNonExistingRoomById() {
        assertThrows(NoSuchElementException.class, () -> roomRepository
            .getById(UUID.fromString("5c3c85ee-c8ad-42e7-b8f9-08f987a91d3e"))
            .orElseThrow());
    }

    @Test
    void shouldPassExistsById() {
        assertTrue(roomRepository.existsById(
            UUID.fromString("dba673f8-0526-4cea-941e-3c8ddd5e4f92"))
        );
    }

    @Test
    void shouldFailByCheckingIfNonExistingRoomExistsById() {
        assertFalse(roomRepository.existsById(
            UUID.fromString("5c3c85ee-c8ad-42e7-b8f9-08f987a91d3e"))
        );
    }

    @Test
    void shouldPassWhenGettingAllRooms() {
        List<RoomEntity> rooms = roomRepository.getAll();
        assertNotNull(rooms);
        assertTrue(rooms.size() > 0);
    }

    @Test
    void shouldPassWhenAddingNewRoom() {
        RoomEntity roomEntity = new RoomEntity(11614, 200.0, 20);
        roomEntity = roomRepository.add(roomEntity);
        RoomEntity checkRoomEntity = roomRepository
            .getById(roomEntity.getId()).orElseThrow();
        assertEquals(roomEntity, checkRoomEntity);
    }

    @Test
    void shouldPassWhenRemovingRoom() {
        RoomEntity roomEntity = new RoomEntity(1615, 200.0, 20);
        roomEntity = roomRepository.add(roomEntity);
        UUID uuid = roomEntity.getId();
        roomRepository.remove(roomEntity);
        assertThrows(NoSuchElementException.class, () -> roomRepository
            .getById(uuid).orElseThrow());
    }

    @Test
    void shouldPassWhenUpdatingRoom() {

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
        assertThrows(NoSuchElementException.class, () -> rentRepository
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
        ClientEntity clientEntity =
            new ClientEntity(null, "userndame", true, "password", "Firstname", "Lastname", "164732", addressEntity);

        roomEntity = roomRepository.add(roomEntity);
        clientEntity = (ClientEntity) userRepository.add(clientEntity);

        RentEntity rentEntity =
            new RentEntity(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 100, clientEntity,
                roomEntity);

        rentEntity = rentRepository.add(rentEntity);
        RentEntity checkRentEntity = rentRepository
            .getById(rentEntity.getId()).orElseThrow();

        assertEquals(rentEntity, checkRentEntity);
    }

    @Test
    void shouldPassWhenRemovingRent() {
        RoomEntity roomEntity = new RoomEntity(166214, 200.0, 20);
        AddressEntity addressEntity = new AddressEntity("city", "street", 1);
        ClientEntity clientEntity =
            new ClientEntity(null, "username23", true, "password", "Firstname", "Lastname", "1645732", addressEntity);

        roomEntity = roomRepository.add(roomEntity);
        clientEntity = (ClientEntity) userRepository.add(clientEntity);

        RentEntity rentEntity =
            new RentEntity(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 100, clientEntity,
                roomEntity);

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

        ClientEntity clientEntity =
            (ClientEntity) userRepository.getById(UUID.fromString("a524d75e-927a-4a10-8c46-6321fff6979e"))
                .orElseThrow();

        RentEntity rentEntity =
            new RentEntity(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 100, clientEntity,
                roomEntity);
        rentEntity = rentRepository.add(rentEntity);

        assertTrue(rentEntity.isBoard());

        rentEntity.setBoard(false);
        rentEntity = rentRepository.update(rentEntity)
            .orElseThrow();

        assertFalse(rentEntity.isBoard());
    }
    //endregion

    // region UserRepository
    // region getUserByUsername
    @Test
    void shouldFindAdminUserByUsernameTest() {
        UserEntity found = userRepository.getUserByUsername("admin").orElseThrow();

        assertNotNull(found);
        assertEquals(AdminEntity.class, found.getClass());
        assertEquals(UUID.fromString("48b0048e-ccf0-43f4-b92b-5a6aad736960"), found.getId());
        assertTrue(found.isActive());
        assertEquals("ADMIN", found.getRole());
    }

    @Test
    void shouldFindEmployeeByUsernameTest() {
        UserEntity found = userRepository.getUserByUsername("employee").orElseThrow();

        assertNotNull(found);
        assertEquals(EmployeeEntity.class, found.getClass());
        assertEquals(UUID.fromString("8936c72e-77f7-4ec0-81d9-d8fcb3870449"), found.getId());
        assertTrue(found.isActive());
        assertEquals("EMPLOYEE", found.getRole());
    }

    @Test
    void shouldFindClientByUsernameTest() {
        UserEntity found = userRepository.getUserByUsername("client").orElseThrow();

        assertNotNull(found);
        assertEquals(ClientEntity.class, found.getClass());
        assertEquals(UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"), found.getId());
        assertTrue(found.isActive());
        assertEquals("CLIENT", found.getRole());
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserWasNotFindByUsernameTest() {
        assertTrue(userRepository.getUserByUsername("asdfasfasfdsa").isEmpty());
    }
    // endregion

    // region add
    @Test
    void shouldAddUserEntityTest() {
        String firstName = "firstname";
        String lastName = "lastname";
        String username = "newEmployee";

        EmployeeEntity userEntity = new EmployeeEntity(null, username, true, "pass", firstName, lastName);

        UserEntity created = userRepository.add(userEntity);

        assertNotNull(created);
        assertEquals(EmployeeEntity.class, created.getClass());
        assertEquals(username, created.getUsername());

        EmployeeEntity createdEmployee = (EmployeeEntity) created;
        assertEquals("EMPLOYEE", created.getRole());
        assertEquals(firstName, createdEmployee.getFirstName());
        assertEquals(lastName, createdEmployee.getLastName());
        assertTrue(createdEmployee.isActive());
    }
    // endregion

    // region remove
    @Test
    @Order(100)
    void shouldRemoveUserEntityTest() {
        UserEntity clientToRemove = userRepository.getUserByUsername("jakub3").orElseThrow();

        assertNotNull(clientToRemove);

        userRepository.remove(clientToRemove);

        assertTrue(userRepository.getUserByUsername("jakub3").isEmpty());
    }
    //endregion

    // region getById
    @Test
    void shouldFindUserEntityByIdTest() {
        UUID id1 = UUID.fromString("48b0048e-ccf0-43f4-b92b-5a6aad736960");
        UserEntity found1 = userRepository.getById(id1)
            .orElseThrow();

        assertNotNull(found1);
        assertEquals("admin", found1.getUsername());
        assertEquals(id1, found1.getId());

        UUID id2 = UUID.fromString("8936c72e-77f7-4ec0-81d9-d8fcb3870449");
        UserEntity found2 = userRepository.getById(id2)
            .orElseThrow();

        assertNotNull(found2);
        assertEquals("employee", found2.getUsername());
        assertEquals(id2, found2.getId());

        UUID id3 = UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396");
        UserEntity found3 = userRepository.getById(id3)
            .orElseThrow();

        assertNotNull(found3);
        assertEquals("client", found3.getUsername());
        assertEquals(id3, found3.getId());
    }

    @Test
    void shouldReturnEmptyWhenUserWasNotFoundByIdTest() {
        assertTrue(userRepository.getById(UUID.randomUUID()).isEmpty());
    }
    // endregion

    // region getAll
    @Test
    @Order(1)
    void shouldGetAllUsersTest() {
        List<UserEntity> userEntityList = userRepository.getAllUsers();

        assertNotNull(userEntityList);
        assertEquals(5, userEntityList.size());
    }
    //endregion

    // region matchUserByUsername
    @Test
    @Order(89)
    void shouldGetUserEntitiesWithMatchingUsernameTest() {
        List<UserEntity> userEntityList = userRepository.matchUserByUsername("jakub");

        assertNotNull(userEntityList);
        assertEquals(2, userEntityList.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoUserEntitiesMatchingUsernameFoundTest() {
        List<UserEntity> list = userRepository.matchUserByUsername("asdfaskhfsadkfsa");

        assertNotNull(list);
        assertEquals(0, list.size());
    }
    // endregion

    // region getUsersByRole
    @Test
    @Order(2)
    void shouldGetUserEntitiesWithRoleTest() {
        List<UserEntity> admins = userRepository.getUsersByRole("ADMIN");

        assertNotNull(admins);
        assertEquals(1, admins.size());

        List<UserEntity> employees = userRepository.getUsersByRole("EMPLOYEE");

        assertNotNull(employees);
        assertEquals(1, employees.size());

        List<UserEntity> clients = userRepository.getUsersByRole("CLIENT");

        assertNotNull(clients);
        assertEquals(3, clients.size());

        List<UserEntity> emptyList = userRepository.getUsersByRole("DFSKdjfas");

        assertNotNull(emptyList);
        assertEquals(0, emptyList.size());
    }
    // endregion

    // region getUsersByRoleAndMatchingUsername
    @Test
    @Order(90)
    void shouldGetUserEntitiesInRoleWithMatchingUsernameTest() {
        List<UserEntity> clients = userRepository.getUsersByRoleAndMatchingUsername("CLIENT", "jakub");

        assertNotNull(clients);
        assertEquals(2, clients.size());
    }
    //endregion

    // region update
    @Test
    @Order(99)
    void shouldUpdateUserEntityTest() {
        UserEntity jakub3 = userRepository.getUserByUsername("jakub3").orElseThrow();

        assertNotNull(jakub3);
        assertEquals(ClientEntity.class, jakub3.getClass());

        jakub3.setActive(false);
        ((ClientEntity) jakub3).setFirstName("Jakub");
        UserEntity updated = userRepository.update(jakub3).orElseThrow();

        assertNotNull(updated);
        assertFalse(updated.isActive());
        assertEquals("Jakub", ((ClientEntity) updated).getFirstName());
    }
    // endregion

    //endregion
}
