package pl.lodz.p.it.tks.ui;

import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomHasActiveReservationsException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.Room;

import java.util.List;

public interface RoomUseCase {
    Room addRoom(Room room) throws CreateRoomException;

    List<Room> getAllRooms();

    Room getRoomById(Long id) throws RoomNotFoundException;

    Room getRoomByNumber(int number) throws RoomNotFoundException;

    List<Rent> getAllRentsOfRoom(Long roomId, Boolean past) throws RoomNotFoundException;

    Room updateRoom(Long id, Room room) throws RoomNotFoundException, UpdateRoomException;

    void removeRoom(Long id) throws RoomHasActiveReservationsException;
}
