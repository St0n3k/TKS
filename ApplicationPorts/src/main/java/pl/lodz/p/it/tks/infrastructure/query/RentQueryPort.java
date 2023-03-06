package pl.lodz.p.it.tks.infrastructure.query;

import pl.lodz.p.it.tks.model.Rent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentQueryPort {
    Optional<Rent> getById(UUID id);
    List<Rent> getAll();
    List<Rent> getByRoomId(UUID roomId);
    List<Rent> getByClientUsername(String username);
    List<Rent> getByClientId(UUID clientId);
    List<Rent> findByRoomAndStatus(UUID roomId, boolean past);
    List<Rent> findByClientAndStatus(UUID clientId, boolean past);

}
