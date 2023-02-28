package pl.lodz.p.it.tks.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.in.RoomQueryPort;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.out.RoomCommandPort;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@RequestScoped
public class RoomService {

    @Inject
    private RoomQueryPort roomQueryPort;

    @Inject
    private RoomCommandPort roomCommandPort;


    /**
     * Method used to save room to database, room number has to be unique, otherwise method will throw exception
     *
     * @param room room to be saved
     * @return status code
     * 201(CREATED) if room was successfully saved,
     * 409(CONFLICT) if room was not saved due to constraints(room id / room number)
     */
    public Room addRoom(Room room) throws CreateRoomException {
        try {
            return roomCommandPort.addRoom(room);
        } catch (Exception e) {
            throw new CreateRoomException();
        }
    }

    /**
     * Method used to get all saved rooms if param number is not set,
     * otherwise it will return room with given room number
     *
     * @return status code
     * 200(OK) and list of all rooms
     * 200(OK) if number parameter was set and room was found
     * 404(NOT_FOUND) if number parameter was set, but room was not found
     */
    public List<Room> getAllRooms() {
        return roomQueryPort.getAllRooms();
    }


    public Room getRoomById(Long id) throws RoomNotFoundException {
        return roomQueryPort.getById(id)
                .orElseThrow(RoomNotFoundException::new);
    }


    public Room getRoomByNumber(int number) throws RoomNotFoundException {
        return roomQueryPort.getByNumber(number)
                .orElseThrow(RoomNotFoundException::new);
    }

//    /**
//     * Endpoint which returns list of rents of given room
//     *
//     * @param roomId room id
//     * @param past flag which indicates if the result will be list of past rents or future rents.
//     * If this parameter is not set, the result of the method will be list of all rents of given room
//     * @return list of rents that meet given criteria
//     */
//    public List<Rent> getAllRentsOfRoom(Long roomId, Boolean past) throws RoomNotFoundException {
//        if (!roomRepository.existsById(roomId)) {
//            throw new RoomNotFoundException();
//        }
//
//        List<Rent> rents;
//        if (past != null) { // find past or active rents
//            rents = rentRepository.findByRoomAndStatus(roomId, past);
//        } else { // find all rents
//            rents = rentRepository.getByRoomId(roomId);
//        }
//        return rents;
//    }
//
//
    /**
     * Endpoint which is used to update room properties
     *
     * @param id id of room to be updated
     * @param room object containing new properties of existing room
     */
    //TODO not working properly due to version field
    public Room updateRoom(Long id, Room room) throws RoomNotFoundException, UpdateRoomException {
        Room existingRoom = roomQueryPort.getById(id)
                .orElseThrow(RoomNotFoundException::new);

        existingRoom.setPrice(room.getPrice() == null ? existingRoom.getPrice() : room.getPrice());
        existingRoom.setSize(room.getSize() == null ? existingRoom.getSize() : room.getSize());
        existingRoom.setRoomNumber(room.getRoomNumber() == null ? existingRoom.getRoomNumber() : room.getRoomNumber());

        try {
            return roomCommandPort.updateRoom(existingRoom)
                    .orElseThrow(UpdateRoomException::new);
        } catch (Exception e) {
            throw new UpdateRoomException();
        }
    }
//
//
//    /**
//     * Endpoint for removing room from database. Room can be removed only if there are no current or future rents
//     *
//     * @param id id of the room to be removed
//     * @return status code
//     * 204(NO_CONTENT) if room was removed or was not found
//     * 409(CONFLICT) if there are current or future rents for room with given id
//     */
//    public void removeRoom(Long id) throws RoomHasActiveReservationsException {
//        Optional<Room> optionalRoom = roomQueryPort.getRoomById(id);
//
//        List<Rent> rentsForRoom = rentRepository.findByRoomAndStatus(room.getId(), false);
//        if (rentsForRoom.isEmpty()) {
//            roomRepository.remove(room);
//        } else {
//            throw new RoomHasActiveReservationsException();
//        }
//    }
}
