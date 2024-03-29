package pl.lodz.p.it.tks.infrastructure.command;

import pl.lodz.p.it.tks.exception.room.UpdateRoomException;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.Room;

import java.util.Optional;

public interface RoomCommandPort {
    Room addRoom(Room room);

    Optional<Room> updateRoom(Room room) throws UpdateRoomException;

    void removeRoom(Room room);

    Apartment addApartment(Apartment apartment);

    Optional<Apartment> updateApartment(Apartment existingApartment);
}
