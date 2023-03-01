package pl.lodz.p.it.tks.infrastructure;

import pl.lodz.p.it.tks.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomQueryPort {
    List<Room> getAllRooms();

    Optional<Room> getById(Long id);

    Optional<Room> getByNumber(int number);

    boolean existsById(Long id);
}
