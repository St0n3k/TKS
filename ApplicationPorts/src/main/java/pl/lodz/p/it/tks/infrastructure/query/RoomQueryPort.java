package pl.lodz.p.it.tks.infrastructure.query;

import pl.lodz.p.it.tks.model.Room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomQueryPort {
    List<Room> getAllRooms();

    Optional<Room> getById(UUID id);

    Optional<Room> getByNumber(int number);

    boolean existsById(UUID id);
}
