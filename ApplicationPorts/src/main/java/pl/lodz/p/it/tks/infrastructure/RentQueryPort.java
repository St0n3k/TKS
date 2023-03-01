package pl.lodz.p.it.tks.infrastructure;

import pl.lodz.p.it.tks.model.Rent;

import java.util.List;
import java.util.Optional;

public interface RentQueryPort {
    Optional<Rent> getById(Long id);
    List<Rent> getAll();
    List<Rent> getByRoomId(Long roomId);
    List<Rent> getByClientUsername(String username);
    List<Rent> getByClientId(Long clientId);
    List<Rent> findByRoomAndStatus(Long roomId, boolean past);
    List<Rent> findByClientAndStatus(Long clientId, boolean past);

}
