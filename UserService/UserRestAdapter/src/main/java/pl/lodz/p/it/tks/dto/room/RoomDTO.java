package pl.lodz.p.it.tks.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.dto.AbstractDTO;
import pl.lodz.p.it.tks.model.Room;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO extends AbstractDTO {

    private Integer roomNumber;

    private Double price;

    private Integer size;


    public RoomDTO(Room room) {
        super(room.getId(), room.getVersion());
        roomNumber = room.getRoomNumber();
        price = room.getPrice();
        size = room.getSize();
    }
}
