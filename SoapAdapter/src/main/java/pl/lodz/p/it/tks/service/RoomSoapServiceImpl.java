package pl.lodz.p.it.tks.service;

import jakarta.inject.Inject;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.validation.Valid;
import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomHasActiveReservationsException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.mapper.RoomSoapMapper;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.ApartmentSoapDTO;
import pl.lodz.p.it.tks.model.CreateApartmentSoapDTO;
import pl.lodz.p.it.tks.model.CreateRoomSoapDTO;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.RoomSoapDTO;
import pl.lodz.p.it.tks.model.UpdateApartmentSoapDTO;
import pl.lodz.p.it.tks.model.UpdateRoomSoapDTO;
import pl.lodz.p.it.tks.ui.command.RoomCommandUseCase;
import pl.lodz.p.it.tks.ui.query.RoomQueryUseCase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebService(name = "RoomSOAP", serviceName = "roomAPI") //, endpointInterface = "pl.lodz.p.it.tks.service.RoomSoapService")
public class RoomSoapServiceImpl implements RoomSoapService {

    @Inject
    private RoomQueryUseCase roomQueryUseCase;

    @Inject
    private RoomCommandUseCase roomCommandUseCase;

    @Inject
    private RoomSoapMapper roomSoapMapper;

    @Override
    public List<RoomSoapDTO> getAllRooms() {
        return roomQueryUseCase.getAllRooms()
                               .stream().map(room -> roomSoapMapper.mapToDto(room))
                               .collect(Collectors.toList());
    }

    @Override
    public RoomSoapDTO getRoomById(@WebParam(name = "id", mode = WebParam.Mode.IN) UUID id)
        throws RoomNotFoundException {
        return roomSoapMapper.mapToDto(roomQueryUseCase.getRoomById(id));
    }

    @Override
    public RoomSoapDTO getRoomByRoomNumber(@WebParam(name = "roomNumber", mode = WebParam.Mode.IN) Integer roomNumber)
        throws RoomNotFoundException {
        return roomSoapMapper.mapToDto(roomQueryUseCase.getRoomByNumber(roomNumber));
    }

    @Override
    public void removeRoom(@WebParam(name = "id", mode = WebParam.Mode.IN) UUID id)
        throws RoomHasActiveReservationsException {
        roomCommandUseCase.removeRoom(id);
    }

    @Override
    public RoomSoapDTO addRoom(@Valid @WebParam(name = "dto", mode = WebParam.Mode.IN) CreateRoomSoapDTO dto)
        throws CreateRoomException {
        Room room = new Room(dto.getRoomNumber(), dto.getPrice(), dto.getSize());
        room = roomCommandUseCase.addRoom(room);
        return roomSoapMapper.mapToDto(room);
    }

    @Override
    public ApartmentSoapDTO addApartment(
        @Valid @WebParam(name = "dto", mode = WebParam.Mode.IN) CreateApartmentSoapDTO dto)
        throws CreateRoomException {
        Apartment apartment = new Apartment(dto.getRoomNumber(), dto.getPrice(), dto.getSize(), dto.getBalconyArea());
        apartment = roomCommandUseCase.addApartment(apartment);
        return new ApartmentSoapDTO(apartment);
    }

    @Override
    public RoomSoapDTO updateRoom(@WebParam(name = "id", mode = WebParam.Mode.IN) UUID id,
                                  @Valid @WebParam(name = "dto", mode = WebParam.Mode.IN) UpdateRoomSoapDTO dto)
        throws RoomNotFoundException, UpdateRoomException {
        Room room = roomCommandUseCase.updateRoom(id, new Room(dto.getRoomNumber(), dto.getPrice(), dto.getSize()));
        return roomSoapMapper.mapToDto(room);
    }

    @Override
    public ApartmentSoapDTO updateApartment(
        @WebParam(name = "id", mode = WebParam.Mode.IN) UUID id,
        @Valid @WebParam(name = "dto", mode = WebParam.Mode.IN) UpdateApartmentSoapDTO dto)
        throws InvalidInputException, RoomNotFoundException, UpdateRoomException {
        Apartment apartment = roomCommandUseCase.updateApartment(id, new Apartment(dto.getRoomNumber(), dto.getPrice(),
                                                                                   dto.getSize(),
                                                                                   dto.getBalconyArea()));
        return new ApartmentSoapDTO(apartment);
    }

}
