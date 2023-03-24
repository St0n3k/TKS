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
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.model.user.UserEntity;
import pl.lodz.p.it.tks.repository.RoomRepository;
import pl.lodz.p.it.tks.repository.impl.RoomRepositoryImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        RoomEntity roomEntity = new RoomEntity(1614, 200.0, 20);
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
}
