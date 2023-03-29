package pl.lodz.p.it.tks.repository;

import pl.lodz.p.it.tks.model.RentEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentRepository extends Repository<RentEntity> {

    List<RentEntity> getByRoomId(UUID roomId);

    List<RentEntity> getByClientUsername(String username);

    List<RentEntity> getByClientId(UUID clientId);

    Optional<RentEntity> update(RentEntity rent);

    boolean removeById(UUID rentId);

    List<RentEntity> findByRoomAndStatus(UUID roomId, boolean past);

    List<RentEntity> findByClientAndStatus(UUID clientId, boolean past);
}
