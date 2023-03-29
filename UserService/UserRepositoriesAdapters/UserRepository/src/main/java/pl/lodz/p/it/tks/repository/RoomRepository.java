package pl.lodz.p.it.tks.repository;

import pl.lodz.p.it.tks.model.RoomEntity;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends Repository<RoomEntity> {

    Optional<RoomEntity> getByRoomNumber(int roomNumber);

    boolean existsById(UUID id);
}
