import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.service.CreateRoomException_Exception;
import pl.lodz.p.it.tks.service.CreateRoomSoapDTO;
import pl.lodz.p.it.tks.service.Room;
import pl.lodz.p.it.tks.service.RoomAPI;
import pl.lodz.p.it.tks.service.RoomNotFoundException_Exception;
import pl.lodz.p.it.tks.service.RoomSOAP;

import java.io.IOException;
import java.net.URL;
import java.util.List;


//@Testcontainers
public class SoapIT {


    private final RoomSOAP roomSOAP;

    public SoapIT() throws IOException {
        URL url = new URL("http://localhost:8080/api/roomAPI?wsdl");

        RoomAPI roomAPI = new RoomAPI(url);
        roomSOAP = roomAPI.getRoomSOAPPort();
    }

    @Test
    void getAllRoomsTest() {
        List<Room> allRooms = roomSOAP.getAllRooms();
        Assertions.assertFalse(allRooms.isEmpty());
    }

    @Test
    void addRoomTest() throws CreateRoomException_Exception, RoomNotFoundException_Exception {
        CreateRoomSoapDTO dto = new CreateRoomSoapDTO();
        dto.setPrice(1);
        dto.setRoomNumber(123456);
        dto.setSize(3);

        roomSOAP.addRoom(dto);

        Room room = roomSOAP.getRoomByRoomNumber(123456);
        Assertions.assertEquals(123456, room.getRoomNumber());
        Assertions.assertEquals(1, room.getPrice());
        Assertions.assertEquals(3, room.getSize());
    }


}
