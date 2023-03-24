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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ArquillianExtension.class)
public class RepositoryIT {

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(RoomRepository.class.getPackage())
                .addPackage(RoomRepositoryImpl.class.getPackage())
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
    void getRoomByNumberTest() {
        RoomEntity roomEntity = roomRepository.getByRoomNumber(836)
                .orElseThrow();

        assertEquals(1, roomEntity.getSize());
        assertEquals(836, roomEntity.getRoomNumber());
        assertEquals(707.19, roomEntity.getPrice());
    }
}
