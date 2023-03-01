package pl.lodz.p.it.tks.repository;

import pl.lodz.p.it.tks.model.RentEntity;

import java.util.List;
import java.util.Optional;

public interface RentRepository extends Repository<RentEntity> {

    List<RentEntity> getByRoomId(Long roomId);

    List<RentEntity> getByClientUsername(String username);

    List<RentEntity> getByClientId(Long clientId);

    Optional<RentEntity> update(RentEntity rent);

    boolean removeById(Long rentId);

    List<RentEntity> findByRoomAndStatus(Long roomId, boolean past);

    List<RentEntity> findByClientAndStatus(Long clientId, boolean past);
}
