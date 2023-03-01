package pl.lodz.p.it.tks.aggregates;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.tks.in.RentQueryPort;
import pl.lodz.p.it.tks.model.Rent;
import pl.lodz.p.it.tks.model.RentEntity;
import pl.lodz.p.it.tks.out.RentCommandPort;
import pl.lodz.p.it.tks.repository.impl.RentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class RentRepositoryAdapter implements RentQueryPort, RentCommandPort {
    @Inject
    private RentRepository rentRepository;

    @Override
    public Optional<Rent> getById(Long id) {
        return rentRepository.getById(id).map(RentEntity::mapToRent);
    }

    @Override
    public List<Rent> getAll() {
//        List<RentEntity> entities = rentRepository.getAll();
//        List<Rent> rents = new ArrayList<>();
//        for (RentEntity e : entities) {
//            rents.add(e.mapToRent());
//        }
//        return rents;
        return rentRepository.getAll()
                .stream()
                .map(RentEntity::mapToRent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rent> getByRoomId(Long roomId) {
        return rentRepository.getByRoomId(roomId).stream().map(RentEntity::mapToRent).collect(Collectors.toList());
    }

    @Override
    public List<Rent> getByClientUsername(String username) {
        return rentRepository.getByClientUsername(username).stream().map(RentEntity::mapToRent).collect(Collectors.toList());
    }

    @Override
    public List<Rent> getByClientId(Long clientId) {
        return rentRepository.getByClientId(clientId).stream().map(RentEntity::mapToRent).collect(Collectors.toList());
    }

    @Override
    public List<Rent> findByRoomAndStatus(Long roomId, boolean past) {
        return rentRepository.findByRoomAndStatus(roomId, past).stream().map(RentEntity::mapToRent).collect(Collectors.toList());
    }

    @Override
    public List<Rent> findByClientAndStatus(Long clientId, boolean past) {
        return rentRepository.findByClientAndStatus(clientId, past).stream().map(RentEntity::mapToRent).collect(Collectors.toList());
    }

    @Override
    public Rent add(Rent rent) {
        return rentRepository.add(new RentEntity(rent)).mapToRent();
    }

    @Override
    public void remove(Rent rent) {
        rentRepository.remove(new RentEntity(rent));
    }

    @Override
    public Optional<Rent> update(Rent rent) {
        return rentRepository.update(new RentEntity(rent)).map(RentEntity::mapToRent);
    }

    @Override
    public boolean removeById(Long rentId) {
        return rentRepository.removeById(rentId);
    }
}
