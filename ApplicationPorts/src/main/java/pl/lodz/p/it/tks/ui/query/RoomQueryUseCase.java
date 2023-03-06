package pl.lodz.p.it.tks.ui.query;

import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.Room;

import java.util.List;

public interface RoomQueryUseCase {
    List<Room> getAllRooms();

    Room getRoomById(Long id) throws RoomNotFoundException;

    Room getRoomByNumber(int number) throws RoomNotFoundException;

    List<Rent> getAllRentsOfRoom(Long roomId, Boolean past) throws RoomNotFoundException;
}
