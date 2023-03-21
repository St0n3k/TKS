package pl.lodz.p.it.tks.service;

import java.util.List;
import java.util.UUID;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.validation.Valid;
import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomHasActiveReservationsException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.model.ApartmentSoapDTO;
import pl.lodz.p.it.tks.model.CreateApartmentSoapDTO;
import pl.lodz.p.it.tks.model.CreateRoomSoapDTO;
import pl.lodz.p.it.tks.model.RoomSoapDTO;
import pl.lodz.p.it.tks.model.UpdateApartmentSoapDTO;
import pl.lodz.p.it.tks.model.UpdateRoomSoapDTO;

@WebService
public interface RoomSoapService {
    @WebMethod
    List<RoomSoapDTO> getAllRooms();

    @WebMethod
    RoomSoapDTO getRoomById(@WebParam(name = "id", mode = WebParam.Mode.IN) UUID id) throws RoomNotFoundException;

    @WebMethod
    void removeRoom(@WebParam(name = "id", mode = WebParam.Mode.IN) UUID id) throws RoomHasActiveReservationsException;

    @WebMethod
    RoomSoapDTO getRoomByRoomNumber(@WebParam(name = "roomNumber", mode = WebParam.Mode.IN) Integer roomNumber)
        throws RoomNotFoundException;

    @WebMethod
    RoomSoapDTO addRoom(@Valid @WebParam(name = "dto", mode = WebParam.Mode.IN) CreateRoomSoapDTO dto)
        throws CreateRoomException;

    @WebMethod
    ApartmentSoapDTO addApartment(@Valid @WebParam(name = "dto", mode = WebParam.Mode.IN) CreateApartmentSoapDTO dto)
        throws CreateRoomException;

    @WebMethod
    RoomSoapDTO updateRoom(@WebParam(name = "id", mode = WebParam.Mode.IN) UUID id,
                           @Valid @WebParam(name = "dto", mode = WebParam.Mode.IN) UpdateRoomSoapDTO dto)
        throws RoomNotFoundException, UpdateRoomException;

    @WebMethod
    ApartmentSoapDTO updateApartment(@WebParam(name = "id", mode = WebParam.Mode.IN) UUID id,
                                     @Valid @WebParam(name = "dto", mode = WebParam.Mode.IN) UpdateApartmentSoapDTO dto)
        throws InvalidInputException, RoomNotFoundException, UpdateRoomException;
}
