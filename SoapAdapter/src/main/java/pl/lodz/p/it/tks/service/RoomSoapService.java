package pl.lodz.p.it.tks.service;

import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import pl.lodz.p.it.tks.model.RoomSoapDTO;
import pl.lodz.p.it.tks.ui.query.RoomQueryUseCase;

import java.util.List;
import java.util.stream.Collectors;

@WebService(serviceName = "RoomAPI")
public class RoomSoapService {

    @Inject
    private RoomQueryUseCase roomQueryUseCase;


    @WebMethod
    public List<RoomSoapDTO> getAllRooms() {
        return roomQueryUseCase.getAllRooms()
                .stream().map(RoomSoapDTO::new)
                .collect(Collectors.toList());
    }

}
