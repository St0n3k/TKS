package pl.lodz.p.it.tks.model;

import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlType
public class RoomSoapDTO extends AbstractSoapDTO {

    private Integer roomNumber;

    private Double price;

    private Integer size;


    public RoomSoapDTO(Room room) {
        super(room.getId(), room.getVersion());
        roomNumber = room.getRoomNumber();
        price = room.getPrice();
        size = room.getSize();
    }
}
