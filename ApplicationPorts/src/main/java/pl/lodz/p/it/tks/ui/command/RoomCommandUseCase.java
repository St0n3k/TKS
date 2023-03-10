package pl.lodz.p.it.tks.ui.command;

import pl.lodz.p.it.tks.exception.room.CreateRoomException;
import pl.lodz.p.it.tks.exception.room.RoomHasActiveReservationsException;
import pl.lodz.p.it.tks.exception.room.RoomNotFoundException;
import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.exception.shared.InvalidInputException;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.Room;

import java.util.UUID;

public interface RoomCommandUseCase {
    Room addRoom(Room room) throws CreateRoomException;

    Room updateRoom(UUID id, Room room) throws RoomNotFoundException, UpdateRoomException;

    void removeRoom(UUID id) throws RoomHasActiveReservationsException;

    Apartment addApartment(Apartment apartment) throws CreateRoomException;

    Apartment updateApartment(UUID id, Apartment apartment) throws RoomNotFoundException, UpdateRoomException, InvalidInputException;
}
