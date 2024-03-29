package pl.lodz.p.it.tks.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomHasActiveReservationsException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.infrastructure.command.RoomCommandPort;
import pl.lodz.p.it.tks.infrastructure.query.RentQueryPort;
import pl.lodz.p.it.tks.infrastructure.query.RoomQueryPort;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.ui.command.RoomCommandUseCase;
import pl.lodz.p.it.tks.ui.query.RoomQueryUseCase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@RequestScoped
public class RoomService implements RoomQueryUseCase, RoomCommandUseCase {

    @Inject
    private RoomQueryPort roomQueryPort;

    @Inject
    private RoomCommandPort roomCommandPort;

    @Inject
    private RentQueryPort rentQueryPort;


    /**
     * Method used to save room to database, room number has to be unique, otherwise method will throw exception.
     *
     * @param room room to be saved
     *
     * @return status code
     *     <ul>
     *         <li>201(CREATED) if room was successfully saved,</li>
     *         <li>409(CONFLICT) if room was not saved due to constraints(room id / room number)</li>
     *     </ul>
     */
    @Override
    public Room addRoom(Room room) throws CreateRoomException {
        try {
            return roomCommandPort.addRoom(room);
        } catch (Exception e) {
            throw new CreateRoomException();
        }
    }

    /**
     * Method used to get all saved rooms if param number is not set,
     * otherwise it will return room with given room number.
     *
     * @return status code <ul>
     *     <li>200(OK) and list of all rooms</li>
     *     <li>200(OK) if number parameter was set and room was found</li>
     *     <li>404(NOT_FOUND) if number parameter was set, but room was not found</li>
     *     </ul>
     */
    @Override
    public List<Room> getAllRooms() {
        return roomQueryPort.getAllRooms();
    }


    @Override
    public Room getRoomById(UUID id) throws RoomNotFoundException {
        return roomQueryPort.getById(id)
            .orElseThrow(RoomNotFoundException::new);
    }


    @Override
    public Room getRoomByNumber(int number) throws RoomNotFoundException {
        return roomQueryPort.getByNumber(number)
            .orElseThrow(RoomNotFoundException::new);
    }

    /**
     * Method which returns list of rents of given room.
     *
     * @param roomId room id
     * @param past flag which indicates if the result will be list of past rents or future rents.
     *     If this parameter is not set, the result of the method will be list of all rents of given room
     *
     * @return list of rents that meet given criteria
     */
    @Override
    public List<Rent> getAllRentsOfRoom(UUID roomId, Boolean past) throws RoomNotFoundException {
        if (!roomQueryPort.existsById(roomId)) {
            throw new RoomNotFoundException();
        }

        List<Rent> rents;
        if (past != null) { // find past or active rents
            rents = rentQueryPort.findByRoomAndStatus(roomId, past);
        } else { // find all rents
            rents = rentQueryPort.getByRoomId(roomId);
        }
        return rents;
    }


    /**
     * Method used to update room properties.
     *
     * @param id id of room to be updated
     * @param room object containing new properties of existing room
     */
    @Override
    public Room updateRoom(UUID id, Room room) throws RoomNotFoundException, UpdateRoomException {
        Room existingRoom = roomQueryPort.getById(id)
            .orElseThrow(RoomNotFoundException::new);

        existingRoom.setPrice(room.getPrice() == null ? existingRoom.getPrice() : room.getPrice());
        existingRoom.setSize(room.getSize() == null ? existingRoom.getSize() : room.getSize());
        existingRoom.setRoomNumber(room.getRoomNumber() == null ? existingRoom.getRoomNumber() : room.getRoomNumber());

        try {
            if (existingRoom instanceof Apartment apartment) {
                return roomCommandPort.updateApartment(apartment)
                    .orElseThrow(UpdateRoomException::new);
            }
            return roomCommandPort.updateRoom(existingRoom)
                .orElseThrow(UpdateRoomException::new);
        } catch (Exception e) {
            throw new UpdateRoomException();
        }
    }


    /**
     * Endpoint for removing room from database. Room can be removed only if there are no current or future rents
     *
     * @param id id of the room to be removed
     */
    @Override
    public void removeRoom(UUID id) throws RoomHasActiveReservationsException {
        Optional<Room> optionalRoom = roomQueryPort.getById(id);
        if (optionalRoom.isEmpty()) {
            return;
        }
        Room room = optionalRoom.get();

        List<Rent> rentsForRoom = rentQueryPort.findByRoomAndStatus(room.getId(), false);
        if (rentsForRoom.isEmpty()) {
            roomCommandPort.removeRoom(room);
        } else {
            throw new RoomHasActiveReservationsException();
        }
    }

    @Override
    public Apartment addApartment(Apartment apartment) throws CreateRoomException {
        try {
            return roomCommandPort.addApartment(apartment);
        } catch (Exception e) {
            throw new CreateRoomException();
        }
    }

    @Override
    public Apartment updateApartment(UUID id, Apartment apartment)
        throws RoomNotFoundException, UpdateRoomException, InvalidInputException {
        Room existingRoom = roomQueryPort.getById(id)
            .orElseThrow(RoomNotFoundException::new);

        if (!(existingRoom instanceof Apartment existingApartment)) {
            throw new InvalidInputException();
        }

        existingApartment.setPrice(apartment.getPrice() == null ? existingApartment.getPrice() : apartment.getPrice());
        existingApartment.setSize(apartment.getSize() == null ? existingApartment.getSize() : apartment.getSize());
        existingApartment.setRoomNumber(
            apartment.getRoomNumber() == null ? existingApartment.getRoomNumber() : apartment.getRoomNumber());
        existingApartment.setBalconyArea(
            apartment.getBalconyArea() == null ? existingApartment.getBalconyArea() : apartment.getBalconyArea());

        try {
            return roomCommandPort.updateApartment(existingApartment)
                .orElseThrow(UpdateRoomException::new);
        } catch (Exception e) {
            throw new UpdateRoomException();
        }
    }
}
