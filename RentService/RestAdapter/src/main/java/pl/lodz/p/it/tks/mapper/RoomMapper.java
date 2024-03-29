package pl.lodz.p.it.tks.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import pl.lodz.p.it.tks.dto.room.ApartmentDTO;
import pl.lodz.p.it.tks.dto.room.RoomDTO;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.Room;

@ApplicationScoped
public class RoomMapper {

    public RoomDTO mapToDto(Room room) {
        if (room instanceof Apartment apartment) {
            return new ApartmentDTO(apartment);
        }
        return new RoomDTO(room);
    }
}
