import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.tks.service.CreateRoomException_Exception;
import pl.lodz.p.it.tks.service.CreateRoomSoapDTO;
import pl.lodz.p.it.tks.service.Room;
import pl.lodz.p.it.tks.service.RoomAPI;
import pl.lodz.p.it.tks.service.RoomHasActiveReservationsException_Exception;
import pl.lodz.p.it.tks.service.RoomNotFoundException_Exception;
import pl.lodz.p.it.tks.service.RoomSOAP;
import pl.lodz.p.it.tks.service.UpdateRoomException_Exception;
import pl.lodz.p.it.tks.service.UpdateRoomSoapDTO;

import java.io.IOException;
import java.net.URL;
import java.util.List;


@Testcontainers
public class SoapIT extends SoapTestcontainersSetup {

    private final RoomSOAP roomSOAP;

    public SoapIT() throws IOException {
        URL url = new URL(String.format("http://localhost:%d/soap/roomAPI?wsdl", payaraPort));

        RoomAPI roomAPI = new RoomAPI(url);
        roomSOAP = roomAPI.getRoomSOAPPort();
    }

    @Test
    void shouldReturnListOfRooms() {
        List<Room> allRooms = roomSOAP.getAllRooms();
        assertFalse(allRooms.isEmpty());
    }

    @Test
    void shouldAddRoom() throws CreateRoomException_Exception, RoomNotFoundException_Exception {
        CreateRoomSoapDTO dto = new CreateRoomSoapDTO();
        dto.setPrice(1);
        dto.setRoomNumber(123456);
        dto.setSize(3);

        roomSOAP.addRoom(dto);

        Room room = roomSOAP.getRoomByRoomNumber(123456);

        assertEquals(123456, room.getRoomNumber());
        assertEquals(1, room.getPrice());
        assertEquals(3, room.getSize());
    }

    @Test
    void shouldGetRoomById() throws RoomNotFoundException_Exception {
        Room room = roomSOAP.getRoomById("9acac245-25b3-492d-a742-4c69bfcb90cf");

        assertEquals(643, room.getRoomNumber());
        assertEquals(250.0F, room.getPrice());
        assertEquals(6, room.getSize());
    }

    @Test
    void shouldFailCreatingRoomWithExistingNumber() {
        CreateRoomSoapDTO dto = new CreateRoomSoapDTO();
        dto.setPrice(1);
        dto.setRoomNumber(643);
        dto.setSize(3);

        assertThrows(CreateRoomException_Exception.class, () -> roomSOAP.addRoom(dto));
    }

    @Test
    void shouldGetRoomByIdFail() {
        assertThrows(RoomNotFoundException_Exception.class,
            () -> roomSOAP.getRoomById("dba537f8-0526-4cea-941e-3c8ddd5e4f92"));
    }

    @Test
    void shouldUpdateRoom() throws RoomNotFoundException_Exception, UpdateRoomException_Exception {
        Room originalRoom = roomSOAP.getRoomById("b9573aa2-42fa-43cb-baa1-42d06e1bdc8d");

        assertNotNull(originalRoom);

        UpdateRoomSoapDTO dto = new UpdateRoomSoapDTO();
        dto.setPrice(6.0);
        dto.setRoomNumber(9876);

        roomSOAP.updateRoom("b9573aa2-42fa-43cb-baa1-42d06e1bdc8d", dto);

        Room room = roomSOAP.getRoomById("b9573aa2-42fa-43cb-baa1-42d06e1bdc8d");

        assertEquals(9876, room.getRoomNumber());
        assertEquals(6.0F, room.getPrice());
        assertEquals(originalRoom.getSize(), room.getSize());
    }

    @Test
    void shouldFailUpdatingRoomNumberDueToExistingRoomNumber() {
        UpdateRoomSoapDTO dto = new UpdateRoomSoapDTO();
        dto.setRoomNumber(836);

        assertThrows(UpdateRoomException_Exception.class,
            () -> roomSOAP.updateRoom("a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9", dto));
    }

    @Test
    void shouldRemoveRoom() throws CreateRoomException_Exception, RoomNotFoundException_Exception,
        RoomHasActiveReservationsException_Exception {
        CreateRoomSoapDTO dto = new CreateRoomSoapDTO();
        dto.setPrice(1);
        dto.setRoomNumber(45612);
        dto.setSize(3);

        Room room = roomSOAP.addRoom(dto);

        roomSOAP.getRoomById(room.getId());

        roomSOAP.removeRoom(room.getId());

        assertThrows(RoomNotFoundException_Exception.class,
            () -> roomSOAP.getRoomById(room.getId()));
    }

    @Test
    void shouldFailRemoveRoomWhenThereAreActiveRentsForIt() throws RoomNotFoundException_Exception {
        Room room = roomSOAP.getRoomById("9acac245-25b3-492d-a742-4c69bfcb90cf");

        assertThrows(RoomHasActiveReservationsException_Exception.class,
            () -> roomSOAP.removeRoom(room.getId()));
    }
}
