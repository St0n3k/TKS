package pl.lodz.p.it.tks.aggregates;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.out.AddRoomPort;
import pl.lodz.p.it.tks.repository.impl.RoomRepository;

@ApplicationScoped
public class RoomRepositoryAdapter implements AddRoomPort {

    @Inject
    private RoomRepository roomRepository;
    @Override
    public Room addRoom(Room room) {
        RoomEntity roomEntity = roomRepository.add(new RoomEntity(room));
        return roomEntity.mapToRoom();
    }

}
