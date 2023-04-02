package pl.lodz.p.it.tks.aggregates;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.tks.infrastructure.command.RoomCommandPort;
import pl.lodz.p.it.tks.infrastructure.query.RoomQueryPort;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.ApartmentEntity;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.repository.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoomRepositoryAdapter implements RoomQueryPort, RoomCommandPort {

    @Inject
    private RoomRepository roomRepository;

    @Override
    public Room addRoom(Room room) {
        return roomRepository.add(new RoomEntity(room)).mapToRoom();
    }

    @Override
    public Optional<Room> updateRoom(Room room) {
        return roomRepository.update(new RoomEntity(room))
            .map(RoomEntity::mapToRoom);
    }

    @Override
    public void removeRoom(Room room) {
        if (room instanceof Apartment apartment) {
            roomRepository.remove(new ApartmentEntity(apartment));
        } else {
            roomRepository.remove(new RoomEntity(room));
        }
    }

    @Override
    public Apartment addApartment(Apartment apartment) {
        return (Apartment) roomRepository.add(new ApartmentEntity(apartment)).mapToRoom();
    }

    @Override
    public Optional<Apartment> updateApartment(Apartment apartment) {
        return roomRepository.update(new ApartmentEntity(apartment))
            .map(roomEntity -> (Apartment) roomEntity.mapToRoom());
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.getAll()
            .stream()
            .map(RoomEntity::mapToRoom)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> getById(UUID id) {
        return roomRepository.getById(id).map(RoomEntity::mapToRoom);
    }

    @Override
    public Optional<Room> getByNumber(int number) {
        return roomRepository.getByRoomNumber(number).map(RoomEntity::mapToRoom);
    }

    @Override
    public boolean existsById(UUID id) {
        return roomRepository.existsById(id);
    }
}
