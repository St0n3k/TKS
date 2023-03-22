import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.tks.service.RoomSOAP;

import java.net.MalformedURLException;


@Testcontainers
public class SoapIT extends SoapTestcontainersSetup {


    private RoomSOAP roomSOAP;

    public SoapIT() throws MalformedURLException {
//        URL url = new URL("http://localhost:8080/soap/roomAPI?wsdl");
//        RoomAPI roomAPI = new RoomAPI(url);
//        roomSOAP = roomAPI.getRoomSOAPPort();
    }

    @Test
    void init() {
//        List<Room> allRooms = roomSOAP.getAllRooms();
//        Assertions.assertFalse(allRooms.isEmpty());
        Assertions.assertTrue(payaraContainer.isRunning());
    }


}
