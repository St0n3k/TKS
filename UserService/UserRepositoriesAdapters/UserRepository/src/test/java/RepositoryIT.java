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
import pl.lodz.p.it.tks.user.model.AbstractEntity;
import pl.lodz.p.it.tks.user.model.users.AdminEntity;
import pl.lodz.p.it.tks.user.model.users.ClientEntity;
import pl.lodz.p.it.tks.user.model.users.EmployeeEntity;
import pl.lodz.p.it.tks.user.model.users.UserEntity;
import pl.lodz.p.it.tks.user.repository.UserRepository;
import pl.lodz.p.it.tks.user.repository.impl.UserRepositoryImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ArquillianExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class RepositoryIT {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            // .addPackages(true, "pl.lodz.p.it.tks")
            .addPackage(UserRepositoryImpl.class.getPackage())
            .addPackage(UserRepository.class.getPackage())
            .addPackage(AbstractEntity.class.getPackage())
            .addPackage(UserEntity.class.getPackage())
            .addAsResource("test-initial_data.sql", "META-INF/sql/initial_data.sql")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("test-web.xml", "web.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private UserRepository userRepository;


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
