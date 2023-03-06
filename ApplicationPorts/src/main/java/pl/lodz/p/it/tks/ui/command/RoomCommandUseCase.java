package pl.lodz.p.it.tks.ui.command;

import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomHasActiveReservationsException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.model.Room;

public interface RoomCommandUseCase {
    Room addRoom(Room room) throws CreateRoomException;

    Room updateRoom(Long id, Room room) throws RoomNotFoundException, UpdateRoomException;

    void removeRoom(Long id) throws RoomHasActiveReservationsException;
}
