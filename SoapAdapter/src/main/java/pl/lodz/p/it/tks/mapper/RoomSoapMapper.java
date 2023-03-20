package pl.lodz.p.it.tks.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.ApartmentSoapDTO;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.RoomSoapDTO;

@ApplicationScoped
public class RoomSoapMapper {
    public RoomSoapDTO mapToDto(Room room) {
        if (room instanceof Apartment apartment) {
            return new ApartmentSoapDTO(apartment);
        }
        return new RoomSoapDTO(room);
    }
}
