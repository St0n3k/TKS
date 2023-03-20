package pl.lodz.p.it.tks.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import pl.lodz.p.it.tks.model.RoomSoapDTO;

import java.util.List;

@WebService
public interface RoomSoapService {
    @WebMethod
    List<RoomSoapDTO> getAllRooms();
}
